package au.org.ala.soils2sat

import grails.converters.JSON

class PlotController {

    def springSecurityService
    def plotService

    def detailsFragment() {

        def plotName = params.plotName;
        def userInstance = springSecurityService.currentUser as User

        [plotName:plotName, userInstance: userInstance, appState: userInstance?.applicationState]
    }

    def findPlotFragment() {
        def userInstance = springSecurityService.currentUser as User
        [userInstance: userInstance, appState: userInstance?.applicationState]
    }

    def plotDataFragment() {

        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def layerNames = appState.layers.collect({ it.name }).join(",")

        def plotName = params.plotName

        def plot = plotService.getPlotSummary(plotName)
        def results = []
        if (layerNames && plot) {
            def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layerNames}/${plot.latitude}/${plot.longitude}")
            results = JSON.parse(url.text)
        }

        [results:results, userInstance: springSecurityService.currentUser, appState: appState]
    }

    def comparePlotsFragment = {

        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def results =[:]
        if (userInstance && appState?.layers && appState?.selectedPlots && appState?.selectedPlots.size() > 1) {
            def layerNames = appState.layers.collect({ it.name }).join(",")
            for (StudyLocation plot : appState.selectedPlots) {
                def plotSummary = plotService.getPlotSummary(plot.name)
                def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layerNames}/${plotSummary.latitude}/${plotSummary.longitude}")
                def plotResults = JSON.parse(url.text)
                def temp = [:]
                plotResults.each {
                    temp[it.field ?: it.layername] = it.value
                }
                results[plot.name] = temp
            }
        }

        [userInstance: userInstance, results: results, appState: appState ]
    }
}
