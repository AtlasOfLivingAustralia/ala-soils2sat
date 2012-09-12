package au.org.ala.soils2sat

import grails.converters.JSON

class MapController {
    
    def springSecurityService
    def plotService
    def layerService

    def index() {
        [userInstance: springSecurityService.currentUser]
    }

    def addLayerFragment() {
    }

    def addLayer() {
        String layerName = params.layerName
        User userInstance = springSecurityService.currentUser as User
        if (layerName && userInstance && !userInstance.layers?.contains(layerName)) {
            userInstance.addToLayers(layerName)
        }
        render([status:'ok'] as JSON)
    }

    def removeLayer() {
        String layerName = params.layerName
        User userInstance = springSecurityService.currentUser as User
        if (layerName && userInstance && userInstance.layers?.contains(layerName)) {
            userInstance.removeFromLayers(layerName)
        }
        render([status:'ok'] as JSON)

    }

    def sideBarFragment() {
        [userInstance: springSecurityService.currentUser]
    }

    def selectPlot() {
        def plotName = params.plotName
        if (plotName) {
            User userInstance = springSecurityService.currentUser
            if (!userInstance.selectedPlots?.contains(plotName)) {
                userInstance.addToSelectedPlots(plotName)
                userInstance.save(flush: true)
            }
        }
        render([status:'ok'] as JSON)
    }

    def selectPlots() {
        def plotNames = params.plotNames?.split(",");
        if (plotNames) {
            User userInstance = springSecurityService.currentUser
            plotNames.each {
                if (!userInstance.selectedPlots?.contains(it)) {
                    userInstance.addToSelectedPlots(it)
                }
            }
            userInstance.save(flush: true)
        }
        render([status:'ok'] as JSON)
    }

    def deselectPlot() {
        def plotName = params.plotName
        if (plotName) {
            User userInstance = springSecurityService.currentUser
            if (userInstance.selectedPlots?.contains(plotName)) {
                userInstance.removeFromSelectedPlots(plotName)
                userInstance.save(flush: true)
            }
        }
        render([status:'ok'] as JSON)
    }

    def clearSelectedPlots() {
        User userInstance = springSecurityService.currentUser
        if (userInstance.selectedPlots) {
            userInstance.selectedPlots?.clear();
            userInstance.save(flush: true)
        }
        render([status:'ok'] as JSON)
    }

    def ajaxSearch() {
        BoundingBox bbox = null
        if (params.top && params.bottom && params.left && params.right) {
            bbox = new BoundingBox(left: params.double("left"), right: params.double("right"), top: params.double("top"), bottom: params.double("bottom"))
            println "*** " + bbox
        }

        def results = plotService.searchPlots(params.q, bbox)

        [results: results]
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
