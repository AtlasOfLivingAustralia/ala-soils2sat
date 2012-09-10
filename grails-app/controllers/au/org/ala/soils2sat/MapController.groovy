package au.org.ala.soils2sat

import grails.converters.JSON

class MapController {
    
    def springSecurityService

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

}
