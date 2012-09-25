package au.org.ala.soils2sat

import grails.converters.JSON

class MapController {
    
    def springSecurityService
    def plotService
    def layerService

    def index() {
        def userInstance = springSecurityService.currentUser as User
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
        render([status: success ? 'ok' : 'failed'] as JSON)
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
        [userInstance: userInstance, appState: userInstance?.applicationState]
    }

    def selectPlot() {
        def plotName = params.plotName
        def success = false
        if (plotName) {
            def userInstance = springSecurityService.currentUser as User
            def appState = userInstance?.applicationState

            appState.lock()

            def existing = appState?.selectedPlots?.find {
                it.name == plotName
            }
            if (!existing) {
                def plot = new StudyLocation(name:plotName)
                appState.addToSelectedPlots(plot)
                appState.save(flush: true)
                success = true
            }
        }
        render([status:success ? 'ok' : 'failed'] as JSON)
    }

    def selectPlots() {
        def plotNames = params.plotNames?.split(",");
        def success = false
        if (plotNames) {
            def userInstance = springSecurityService.currentUser as User
            def appState = userInstance.applicationState
            appState.lock()
            plotNames.each { plotName ->
                def existing = appState?.selectedPlots.find {
                    it.name == plotName
                }
                if (!existing) {
                    def studyLocation = new StudyLocation(name:plotName)
                    appState.addToSelectedPlots(studyLocation)
                }
            }
            appState?.save(flush: true, failOnError: true)
            success = true
        }
        render([status: success ? 'ok' : 'failed'] as JSON)
    }

    def deselectPlot() {
        def plotName = params.plotName
        def success = false
        if (plotName) {
            def userInstance = springSecurityService.currentUser as User
            def appState = userInstance?.applicationState
            appState.lock()
            def existing = appState?.selectedPlots?.find {
                it.name == plotName
            }
            if (existing) {
                appState.removeFromSelectedPlots(existing)
                userInstance.save(flush: true)
                success = true
            }
        }
        render([status:success ? 'ok' : 'failed'] as JSON)
    }

    def clearSelectedPlots() {
        def success = false
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        if (appState?.selectedPlots) {
            appState.selectedPlots.clear();
            appState.save(flush: true)
        }
        render([status:success ? 'ok' : 'failed'] as JSON)
    }

    def ajaxSetLayerVisibility() {
        def layerName = params.layerName
        def visibility = params.boolean("visibility") ?: false
        def userInstance = springSecurityService.currentUser as User

        if (layerName && userInstance) {
            def layer = userInstance.applicationState?.layers?.find { it.name == layerName }
            if (layer) {
                layer.visible = visibility
                layer.save(flush: true, failOnError: true)
            }
        }
        render([status:'ok'] as JSON)
    }

    def ajaxSearch() {
        BoundingBox bbox = null
        if (params.top && params.bottom && params.left && params.right) {
            bbox = new BoundingBox(left: params.double("left"), right: params.double("right"), top: params.double("top"), bottom: params.double("bottom"))
        }

        def results = plotService.searchPlots(params.q, bbox)
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        [results: results, userInstance: userInstance, appState: appState]
    }

    def ajaxPlotHover() {
        def plotName = params.plotName
        PlotSearchResult plot = null
        if (plotName) {
            plot = plotService.getPlotSummary(plotName)
        }

        [plot: plot, plotName: plotName]
    }

    def layerInfoFragment() {
        def layerName = params.layerName;
        def info = [:]
        if (layerName) {
            info = layerService.getLayerInfo(layerName)
        }
        [layerName: layerName, layerInfo: info]
    }

}
