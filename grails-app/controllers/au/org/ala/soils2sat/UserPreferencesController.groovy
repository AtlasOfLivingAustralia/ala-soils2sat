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

class UserPreferencesController {

    def springSecurityService

    def listLayerSets() {
        def userInstance = springSecurityService.currentUser as User
        def layerSets = LayerSet.findAllByUser(userInstance)

        [layerSets: layerSets, userInstance: userInstance]
    }

    def newLayerSet() {
        def userInstance = springSecurityService.currentUser as User
        def layerSet = new LayerSet(user: userInstance, name: "<new layer set>")
        layerSet.save(flush: true, failOnError: true)
        redirect action: 'listLayerSets'
    }

    def deleteLayerSet() {
        def userInstance = springSecurityService.currentUser as User
        def layerSetId = params.int("layerSetId")
        if (layerSetId && userInstance) {
            def layerSet = LayerSet.get(layerSetId)
            if (layerSet && layerSet.user == userInstance) {
                layerSet.delete()
            }
        }

        redirect action: 'listLayerSets'
    }

    def editLayerSet() {
        def userInstance = springSecurityService.currentUser as User
        def layerSetId = params.int("layerSetId")
        if (layerSetId && userInstance) {
            def layerSet = LayerSet.get(layerSetId)
            if (layerSet && layerSet.user == userInstance) {
                return [layerSet: layerSet, userInstance: userInstance]
            }
        }
        redirect action: 'listLayerSets'
    }

    def updateLayerSet() {
        def userInstance = springSecurityService.currentUser as User
        def layerSetId = params.int("layerSetId")
        if (layerSetId && userInstance) {
            def layerSet = LayerSet.get(layerSetId)
            if (layerSet && layerSet.user.username == userInstance.username) {
                layerSet.name = params.name
                layerSet.description = params.description
                layerSet.save(flush:  true, failOnError: true)
                flash.message = "Layer set '${layerSet.name}' updated."
            }
        }
        redirect action: 'editLayerSet', params: [layerSetId: layerSetId]
    }

    def addLayerToLayerSet() {
        def userInstance = springSecurityService.currentUser as User
        def layerSetId = params.int("layerSetId")
        def layerName = params.layerName
        def layerSet = LayerSet.findByIdAndUser(layerSetId, userInstance)

        if (layerSet && layerName) {
            layerSet.addToLayers(layerName)
            layerSet.save(flush: true, failOnError: true)
            flash.message = "Layer '${layerName}' added to set '${layerSet.name}'"
        }

        redirect controller: 'userPreferences', action: 'editLayerSet', params: [layerSetId: layerSetId]
    }

    def removeLayerFromLayerSet() {
        def userInstance = springSecurityService.currentUser as User
        def layerSetId = params.int("layerSetId")
        def layerName = params.layerName
        def layerSet = LayerSet.findByIdAndUser(layerSetId, userInstance)

        if (layerSet && layerName) {
            layerSet.removeFromLayers(layerName)
            layerSet.save(flush: true, failOnError: true)
            flash.message = "Layer '${layerName}' has been removed from set '${layerSet.name}'"
        }

        redirect controller: 'userPreferences', action: 'editLayerSet', params: [layerSetId: layerSetId]
    }
}
