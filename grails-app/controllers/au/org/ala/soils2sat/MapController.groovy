/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.soils2sat

import grails.converters.JSON

class MapController {

    static _lock = new String("lock")
    
    def springSecurityService
    def studyLocationService
    def layerService

    def index() {
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        if (appState) {
            appState.lock()
            appState.lastLogin = Calendar.instance.time
            appState.save(flush: true, failOnError: true)
        }

        [userInstance: userInstance , appState: userInstance?.applicationState]
    }

    def addLayerFragment() {
    }

    def addLayer() {
        String layerName = params.layerName
        User userInstance = springSecurityService.currentUser as User
        boolean success = false

        if (layerName && userInstance) {
            def appState = userInstance.applicationState
            appState.lock()
            def existing = appState.layers.find {
                it.name == layerName
            }
            if (!existing) {
                def visible = params.boolean("addToMap")
                def layer = new EnvironmentalLayer(name: layerName, visible: visible)
                appState.addToLayers(layer)
                appState.save(flush: true, failOnError: true)
                success = true
            }

        }

        def style = LayerStyle.findByLayerName(layerName)?.style ?: ''

        render([status: success ? 'ok' : 'failed', style: style] as JSON)
    }

    def removeLayer() {
        String layerName = params.layerName
        User userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def existing = appState.layers.find {
            it.name == layerName
        }
        boolean success = false
        if (layerName && userInstance && existing) {
            appState.removeFromLayers(existing)
            success = true
        }
        render([status:success ? 'ok' : 'failed'] as JSON)
    }

    def removeAllLayers() {
        User userInstance = springSecurityService.currentUser as User
        def success = false
        if (userInstance) {
            def appState = userInstance.applicationState
            appState?.layers?.clear()
            appState?.save(flush: true, failOnError: true)
            success = true
        }
        render([status: success ? 'ok':'failed'] as JSON)
    }

    def sideBarFragment() {

        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        validateSelectedVisits(appState)

        [userInstance: userInstance, appState: appState ]
    }

    private void validateSelectedVisits(UserApplicationState appState) {
        def purgeList = []
        appState.selectedVisits?.each { visit ->
            def details = studyLocationService.getStudyLocationVisitDetails(visit.studyLocationVisitId)
            if (!details) {
                purgeList.add visit
            }
        }

        if (purgeList) {
            purgeList.each {
                appState.removeFromSelectedVisits(it)
            }
            appState.save(failOnError: true)
        }

    }

    def ajaxSetLayerVisibility() {
        def layerName = params.layerName
        def visibility = params.boolean("visibility") ?: false
        def userInstance = springSecurityService.currentUser as User
        def opacity = 1.0
        if (layerName && userInstance) {
            def layer = userInstance.applicationState?.layers?.find { it.name == layerName }
            if (layer) {
                layer.visible = visibility
                layer.save(flush: true, failOnError: true)
                opacity = layer.opacity
            }
        }
        def style = LayerStyle.findByLayerName(layerName)?.style ?: ''
        render([status:'ok', opacity: opacity, style: style] as JSON)
    }

    def ajaxPlotHover() {
        def studyLocationName = params.studyLocationName
        def studyLocation = null
        if (studyLocationName) {
            studyLocation = studyLocationService.getStudyLocationDetails(studyLocationName)
        }

        [studyLocation: studyLocation, studyLocationName: studyLocationName]
    }

    def layerInfoFragment() {
        def layerName = params.layerName;
        def info = [:]
        if (layerName) {
            info = layerService.getLayerInfo(layerName)
        }
        [layerName: layerName, layerInfo: info]
    }

    def ajaxSaveCurrentExtent() {
        def top = params.double("top")
        def left = params.double("left")
        def bottom = params.double("bottom")
        def right = params.double("right")

        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        def success = false;


        if (appState && top && left && bottom && right) {
            synchronized (_lock) {
                appState.lock()

                if (!appState.viewExtent) {
                    def extent = new Bounds(top: top, left: left, right: right, bottom:bottom)
                    extent.save(flush:true, failOnError: true)
                    appState.setViewExtent(extent)
                } else {


                    appState.viewExtent.top = top
                    appState.viewExtent.left = left
                    appState.viewExtent.bottom = bottom
                    appState.viewExtent.right = right
                }

                appState.save(flush: true)
                success = true
            }
        }

        render([status: success ? 'ok' : 'failed'] as JSON)
    }

    def ajaxSaveLayerOpacity() {
        def layerName = params.layerName
        def opacity = params.float("opacity")

        boolean success = false

        def userInstance = springSecurityService.currentUser as User
        if (layerName) {
            def layerInstance = userInstance.applicationState?.layers?.find { it.name == layerName }
            if (layerInstance) {
                layerInstance.opacity = opacity
                layerInstance.save(flush: true, failOnError: true)
                success= true
            }
        }
        render([status: success ? 'ok' : 'failed'] as JSON)
    }

    def browseLayersFragment() {
        def layers = layerService.getAllLayers()

        def root = new LayerTreeNode(label: 'Unclassified')

        layers.each {
            if (it.classification1) {
                def topLevelFolder =  root.getOrAddFolder(it.classification1)

                if (it.classification2) {
                    def secondLevel = topLevelFolder.getOrAddFolder(it.classification2)
                    secondLevel.addLayer(it)
                } else {
                    topLevelFolder.addLayer(it)
                }
            } else {
                root.addLayer(it)
            }
        }

        [layerTree: root]
    }

    def layerSummaryFragment() {
        def layerName = params.layerName;
        def info = [:]
        LayerDefinition layerDefinition = null
        if (layerName) {
            info = layerService.getLayerInfo(layerName)
            layerDefinition = new LayerDefinition()
            info.each {
                if (it.value && layerDefinition.hasProperty(it.key)) {
                    layerDefinition[it.key] = it.value
                }
            }
        }
        [layerName: layerName, layerDefinition: layerDefinition]
    }

    def layerToolsFragment() {
        def layerName = params.layerName;
        def userInstance = springSecurityService.currentUser as User
        if (layerName) {
            def layerInstance = userInstance.applicationState?.layers?.find { it.name == layerName }
            if (layerInstance) {
                def layerInfo = layerService.getLayerInfo(layerName)
                return [layerInstance: layerInstance, layerName: layerName, layerInfo: layerInfo]
            }
        }
    }

    def layerSetsFragment() {
        def globalLayerSets = LayerSet.findAllWhere(user: null)
        def userInstance = springSecurityService.currentUser as User
        def userLayerSets = LayerSet.findAllByUser(userInstance)
        [globalLayerSets: globalLayerSets, userLayerSets: userLayerSets]
    }

    def layerSetSummaryFragment() {
        def layerSet = LayerSet.get(params.int("layerSetId"))
        def layerDescriptions = [:]
        layerSet?.layers?.each {
            def layerInfo = layerService.getLayerInfo(it)
            layerDescriptions[it] = layerInfo.displayname
        }
        [layerSet: layerSet, layerDescriptions: layerDescriptions]
    }

    def addLayerSet() {
        def layerSet = LayerSet.get(params.int("layerSetId"))
        def replaceExisting = params.boolean("replaceExisting")
        def user = springSecurityService.currentUser as User
        def success = false
        if (user && layerSet) {

            def appState = user.applicationState;
            appState.lock();

            if (replaceExisting) {
                appState.layers.clear();
                appState.save(flush: true)
            }

            layerSet.layers.each { layerName ->

                def existing = appState.layers.find {
                    it.name == layerName
                }
                if (!existing) {
                    def layer = new EnvironmentalLayer(name: layerName, visible: false)
                    appState.addToLayers(layer)
                }
            }

            appState.save(flush: true, failOnError: true)
            success = true
        }

        render([status: success ? 'ok' : 'failed'] as JSON)
    }

    def ajaxSetSelectedSidebarTab() {
        def success = false
        try {
            def user = springSecurityService.currentUser as User
            def appState = user.applicationState;
            appState.lock();
            appState.sidebarSelectedTab = params.selectedTab
            appState.save(flush: true, failOnError: true)
            success = true
        } finally {
            render([status: success ? 'ok' : 'failed'] as JSON)
        }
    }

}
