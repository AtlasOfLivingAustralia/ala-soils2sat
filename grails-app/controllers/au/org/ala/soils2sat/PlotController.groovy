package au.org.ala.soils2sat

import grails.converters.JSON

class PlotController {

    def springSecurityService
    def plotService

    def detailsFragment() {

        def plotName = params.plotName;
        def userInstance = springSecurityService.currentUser as User

        [plotName:plotName, userInstance: userInstance, results: results]
    }

    def findPlotFragment() {
        [userInstance:  springSecurityService.currentUser]
    }

    def plotDataFragment() {

        def layers = springSecurityService.currentUser.layers.join(",")

        def plotName = params.plotName

        def plot = plotService.getPlotSummary(plotName)
        def results = []
        if (layers && plot) {
            def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layers}/${plot.latitude}/${plot.longitude}")
            results = JSON.parse(url.text)
        }

        [results:results, userInstance: springSecurityService.currentUser]
    }

    def comparePlotsFragment = {

        def userInstance = springSecurityService.currentUser as User

        def results =[:]
        def layers = userInstance.layers.join(",")
        for (String plotName : userInstance.selectedPlots) {
            def plot = plotService.getPlotSummary(plotName)
            def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layers}/${plot.latitude}/${plot.longitude}")
            def plotResults = JSON.parse(url.text)
            def temp = [:]
            plotResults.each {
                temp[it.field ?: it.layername] = it.value
            }
            results[plotName] = temp
        }

        [userInstance: springSecurityService.currentUser, results: results]
    }
}
