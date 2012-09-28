package au.org.ala.soils2sat

import grails.converters.JSON

class PlotController {

    def springSecurityService
    def plotService

    def getPlots() {
        def results = plotService.getPlots()

        render (results as JSON)
    }

    def getSelectedPlots() {
        def user = springSecurityService.currentUser as User
        def candidates = plotService.getPlots()
        def results = []
        if (user) {
            candidates.each {
                if (user.applicationState?.containsPlot(it.siteName)) {
                    results.add(it)
                }
            }
        }

        render (results as JSON)
    }

    def getUserDisplayedPlots() {
        def user = springSecurityService.currentUser as User
        def candidates = plotService.getPlots()
        def results = []
        if (user && user.applicationState?.plotOnlySelectedLocations) {
            candidates.each {
                if (user.applicationState?.containsPlot(it.siteName)) {
                    results.add(it)
                }
            }
        } else {
            results = candidates
        }

        render (results as JSON)
    }

    def getPlotSummary() {
        def plotName = params.plotName
        if (plotName) {

        }
    }

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

    def findPlotsResultsFragment() {
        BoundingBox bbox = null
        if (params.top && params.bottom && params.left && params.right) {
            bbox = new BoundingBox(left: params.double("left"), right: params.double("right"), top: params.double("top"), bottom: params.double("bottom"))
        }

        def results = plotService.searchPlots(params.q, bbox)
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        [results: results, userInstance: userInstance, appState: appState]
    }

    def ajaxSetPlotSelectedOnly() {
        def visibility = params.boolean("plotSelected") ?: false
        def userInstance = springSecurityService.currentUser as User
        def success = false;
        if (userInstance) {
            def appState = userInstance.applicationState
            appState.lock()
            appState.plotOnlySelectedLocations = visibility
            userInstance.save(flush: true, failOnError: true)
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

}
