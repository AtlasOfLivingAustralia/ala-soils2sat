package au.org.ala.soils2sat

import grails.converters.JSON
import au.com.bytecode.opencsv.CSVWriter

class PlotController {

    def springSecurityService
    def plotService
    def biocacheService

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

    private Map getCompareData(User userInstance) {
        def appState = userInstance?.applicationState

        def data =[:]
        def fieldNames = ['latitude', 'longitude']
        if (userInstance && appState?.layers && appState?.selectedPlots && appState?.selectedPlots.size() > 1) {
            def layerNames = appState.layers.collect({ it.name }).join(",")
            for (StudyLocation plot : appState.selectedPlots) {
                def plotSummary = plotService.getPlotSummary(plot.name)
                def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layerNames}/${plotSummary.latitude}/${plotSummary.longitude}")
                def plotResults = JSON.parse(url.text)
                def temp = [:]
                temp.latitude = plotSummary.latitude
                temp.longitude = plotSummary.longitude
                plotResults.each {
                    def fieldName = it.field ?: it.layername
                    if (!fieldNames.contains(fieldName)) {
                        fieldNames << fieldName
                    }
                    temp[fieldName] = it.value
                }
                data[plot.name] = temp
            }
        }

        return [data: data, fieldNames: fieldNames]
    }

    def comparePlotsFragment = {
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        def results = getCompareData(userInstance)
        [userInstance: userInstance, results: results, appState: appState ]
    }

    def compareTaxaFragment = {

        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def results = [:]

        appState.selectedPlots.each { plot ->
            def plotSummary = plotService.getPlotSummary(plot.name)
            def plotTaxaList = biocacheService.getTaxaNamesForLocation(plotSummary.latitude, plotSummary.longitude, 10, params.rank ?: 'family')
            results[plot.name] = plotTaxaList
        }

        [results:results, appState: appState, userInstance: userInstance]
    }

    def exportComparePlots = {

        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        def results = getCompareData(userInstance)

        response.setHeader("Content-Disposition", "attachment;filename=compare.txt");
        response.setContentType("text/plain");

        def writer = new CSVWriter(response.writer, (char) ',')
        def columnHeaders = ["field"]

        appState.selectedPlots.each {
            columnHeaders << it.name
        }
        writer.writeNext(columnHeaders as String[])

        results.fieldNames.each { fieldName ->
            def lineItems = [fieldName]
            appState.selectedPlots.each { plot ->
                def value = results.data[plot.name][fieldName]
                lineItems << value ?: ''
            }
            writer.writeNext(lineItems as String[])
        }

        writer.flush()
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
