package au.org.ala.soils2sat

import grails.converters.JSON
import au.com.bytecode.opencsv.CSVWriter
import org.apache.commons.lang.WordUtils

import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry

class StudyLocationController {

    def springSecurityService
    def studyLocationService
    def biocacheService
    def settingService
    def extractService

    def getPlots() {
        def results = studyLocationService.getStudyLocations()

        render (results as JSON)
    }

    def getUserDisplayedPoints() {
        def user = springSecurityService.currentUser as User
        def candidates = studyLocationService.getStudyLocations()
        def results = []
        def plotSelectedOnly = user && user.applicationState?.plotOnlySelectedLocations
        def appState = user.applicationState

        def source = appState.selectedPlotNames

        candidates.each { candidate ->
            // check if in the 'selected' source
            candidate.selected = source.find { it == candidate.studyLocationName }

            if (!plotSelectedOnly || candidate.selected && !(results.contains(candidate))) {
                results << candidate
            }
        }

        // Sort the results so that selected plots get rendered last, and therefore on top...
        results = results.sort { it.selected }

        render (results as JSON)
    }

    def synopsisFragment() {
        def studyLocationName = params.studyLocationName;
        def userInstance = springSecurityService.currentUser as User

        [studyLocationName:studyLocationName, userInstance: userInstance, appState: userInstance?.applicationState]
    }


    def studyLocationDataFragment() {

        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def layerNames = appState.layers.collect({ it.name }).join(",")

        def studyLocationName = params.studyLocationName

        def studyLocation = studyLocationService.getStudyLocationSummary(studyLocationName)
        def results = []
        if (layerNames && studyLocation) {
            def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layerNames}/${studyLocation.latitude}/${studyLocation.longitude}")
            results = JSON.parse(url.text)

        }

        [results:results, userInstance: springSecurityService.currentUser, appState: appState, studyLocation: studyLocation]
    }

    private Map getCompareData(User userInstance) {
        def appState = userInstance?.applicationState
        return extractService.getLayerDataForLocations(appState.selectedPlotNames, appState.layers)
    }

    def compareStudyLocationsFragment = {
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        def results = getCompareData(userInstance)
        [userInstance: userInstance, results: results, appState: appState ]
    }

    def compareTaxaFragment = {

        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def results = [:]

        appState.selectedPlotNames.each { studyLocation ->
            def studyLocationSummary = studyLocationService.getStudyLocationSummary(studyLocation)
            def studyLocationTaxaList = biocacheService.getTaxaNamesForLocation(studyLocationSummary.latitude, studyLocationSummary.longitude)
            results[studyLocation] = studyLocationTaxaList
        }

        if (params.diffMode?.toLowerCase() == 'intersect') {
            def candidateEntry = results.max({ it.value.size() })

            def newList = []
            candidateEntry.value.each { taxa ->
                def include = true
                appState.selectedPlotNames.each { studyLocation ->
                    if (studyLocation != candidateEntry.key) {
                        def list = results[studyLocation]
                        if (!list.contains(taxa)) {
                            include = false
                        }
                    }
                }

                if (include) {
                    newList << taxa
                }
            }

            appState.selectedPlotNames?.each { studyLocation ->
                results[studyLocation] = newList
            }

        } else if (params.diffMode?.toLowerCase() == 'inverseintersect') {

            def newResults = [:]

            appState.selectedPlotNames?.each { studyLocation ->
                def newList = []
                def candidateList = results[studyLocation]
                candidateList.each { taxa ->
                    def include = true
                    results.each { kvp ->
                        if (kvp.key != studyLocation) {
                            if (kvp.value.contains(taxa)) {
                                include = false
                            }
                        }
                    }
                    if (include) {
                        newList << taxa
                    }
                }
                newResults[studyLocation] = newList
            }
            results = newResults
        }

        [results:results, appState: appState, userInstance: userInstance]
    }

    def exportComparePlots = {

        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState


        response.setHeader("Content-Disposition", "attachment;filename=CompareExport.zip")

        response.setContentType("application/zip")

        // First up write out the main tasks file -all the remaining fields are single value only
        def zipStream = new ZipOutputStream(response.getOutputStream())
        OutputStream bos = new BufferedOutputStream(zipStream);
        OutputStreamWriter outputwriter = new OutputStreamWriter(bos);
        CSVWriter writer = new CSVWriter(outputwriter);
        // Layer data
        zipStream.putNextEntry(new ZipEntry("environmentalLayers.csv"));
        exportCompareLayers(writer, userInstance)
        writer.flush()
        zipStream.closeEntry();
        // Taxa data
        zipStream.putNextEntry(new ZipEntry("taxa.csv"));
        exportCompareTaxa(writer, userInstance)
        writer.flush()
        zipStream.closeEntry();

        // clean up
        zipStream.close();
    }

    private exportCompareTaxa(CSVWriter writer, User userInstance) {
        def appState = userInstance?.applicationState

        def results = [:]

        appState.selectedPlotNames.each { studyLocation ->
            def studyLocationSummary = studyLocationService.getStudyLocationSummary(studyLocation)
            def studyLocationTaxaList = biocacheService.getTaxaNamesForLocation(studyLocationSummary.latitude, studyLocationSummary.longitude)
            results[studyLocation] = studyLocationTaxaList
        }

        def columnHeaders = results.keySet().toArray() as String[]
        writer.writeNext(columnHeaders)

        def finished = false

        def rowIndex = 0
        while (!finished) {
            finished = true
            def values= []
            appState.selectedPlotNames.each { studyLocation ->
                def value = null
                def fieldList = results[studyLocation] as List
                if (fieldList.size() > rowIndex) {
                    value = fieldList[rowIndex]
                    finished = false;
                }
                values << value ?: ''
            }
            if (!finished) {
                writer.writeNext(values as String[])
            }
            rowIndex++
        }
    }

    private exportCompareLayers(CSVWriter writer, User userInstance) {

        def appState = userInstance.applicationState
        def results = getCompareData(userInstance)
        def columnHeaders = ["field"]
        appState.selectedPlotNames?.each {
            columnHeaders << it
        }
        writer.writeNext(columnHeaders as String[])
        results.fieldNames.each { fieldName ->
            def lineItems = [fieldName]
            appState.selectedPlotNames?.each { studyLocation ->
                def value = results.data[studyLocation][fieldName]
                lineItems << value ?: ''
            }
            writer.writeNext(lineItems as String[])
        }
    }

    def ajaxSetStudyLocationSelectedOnly() {
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

    def deselectStudyLocationVisit() {
        def studyLocationVisitId = params.studyLocationVisitId
        def success = false

        if (studyLocationVisitId) {
            def userInstance = springSecurityService.currentUser as User
            def appState = userInstance?.applicationState

            appState.lock()

            def existing = appState?.selectedVisits?.find {
                it.studyLocationVisitId == studyLocationVisitId
            }

            if (existing) {
                appState.removeFromSelectedVisits(existing)
                appState.save(flush: true)
                success = true
            }
        }
        render([status:success ? 'ok' : 'failed'] as JSON)
    }

    def selectStudyLocationVisit() {
        def studyLocationVisitId = params.studyLocationVisitId
        def studyLocationName = params.studyLocationName
        def success = false

        if (studyLocationVisitId) {
            def userInstance = springSecurityService.currentUser as User
            def appState = userInstance?.applicationState

            appState.lock()

            def existing = appState?.selectedVisits?.find {
                it.studyLocationVisitId == studyLocationVisitId
            }

            if (!existing) {

                def visit = StudyLocationVisit.findByStudyLocationVisitId(studyLocationVisitId)
                if (!visit) {
                    visit = new StudyLocationVisit(studyLocationVisitId: studyLocationVisitId, studyLocationName: studyLocationName)
                }
                appState.addToSelectedVisits(visit)
                appState.save(flush: true)
                success = true
            }
        }
        render([status:success ? 'ok' : 'failed'] as JSON)
    }

    def selectStudyLocationVisits() {
        def studyLocationVisitIds = params.studyLocationVisitIds?.split(",");
        def success = false
        if (studyLocationVisitIds) {
            def userInstance = springSecurityService.currentUser as User
            def appState = userInstance.applicationState
            appState.lock()
            studyLocationVisitIds.each { visitId ->
                def existing = appState?.selectedVisits?.find {
                    it.studyLocationVisitId == visitId
                }
                if (!existing) {
                    // Can't do this until we get a service method to return visit details from an id!
                }
            }
            appState?.save(flush: true, failOnError: true)
            success = true
        }
        render([status: success ? 'ok' : 'failed'] as JSON)
    }

    def clearSelectedStudyLocationVisits() {
        def success = false
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        if (appState?.selectedVisits) {
            appState.selectedVisits.clear();
            appState.save(flush: true)
        }
        render([status:success ? 'ok' : 'failed'] as JSON)
    }

    def studyLocationSummary() {
        def studyLocationName = params.studyLocationName as String
        def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def isSelected = appState.selectedPlotNames.find {
            it == studyLocationName
        } != null

        [studyLocationDetails:studyLocationDetails, studyLocationName: studyLocationName, isSelected: isSelected != null]
    }

    def studyLocationVisitsFragment() {
        def studyLocationName = params.studyLocationName
        def studyLocationVisits = studyLocationService.getStudyLocationVisits(studyLocationName)
        def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        [studyLocation:studyLocationDetails, studyLocationName: studyLocationName, visitSummaries: studyLocationVisits, userInstance: userInstance, appState: appState]
    }

    def studyLocationLayersFragment() {

        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def studyLocationName = params.studyLocationName
        def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
        def layerData = [:]
        if (studyLocationDetails) {
            def layerNames = appState.layers.collect({ it.name }).join(",")
            if (layerNames) {
                def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layerNames}/${studyLocationDetails.latitude}/${studyLocationDetails.longitude}")
                def studyLocationResults = JSON.parse(url.text)
                studyLocationResults.each {
                    def fieldName = it.layername ?: it.field
                    layerData[fieldName] = "${it.value}${it.units? ' (' + it.units + ')' :''}"
                }
            }
        }

        [layerData: layerData, studyLocationName: studyLocationName, studyLocationDetails: studyLocationDetails]
    }

    def studyLocationTaxaFragment() {
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def studyLocationName = params.studyLocationName
        def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)

        def studyLocationTaxaList = biocacheService.getTaxaNamesForLocation(studyLocationDetails.latitude, studyLocationDetails.longitude)

        [studyLocationName: studyLocationName, studyLocationDetails: studyLocationDetails, taxaList: studyLocationTaxaList, rank: settingService.observationsRank, radius: settingService.observationRadius]
    }

    def studyLocationVisitSummary() {
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def studyLocationVisitId = params.studyLocationVisitId

        // TODO: Remove this once visits work from the service properly
        studyLocationVisitId = '2760'

        def visitDetail = studyLocationService.getVisitDetails(studyLocationVisitId)
        def studyLocationName = visitDetail?.studyLocationName
        def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)

        def isSelected = appState.selectedVisits.find { it.studyLocationVisitId == studyLocationVisitId }

        [studyLocationName: studyLocationName, studyLocationDetails: studyLocationDetails, visitDetail: visitDetail, isSelected: isSelected]
    }

    def ajaxSelectedStudyLocationsFragment() {
        def userInstance = springSecurityService.currentUser as User
        [userInstance: userInstance, appState: userInstance?.applicationState]
    }

    def samplingUnitDetail() {

        def visitId = params.studyLocationVisitId
        def samplingUnit = params.samplingUnit
        def studyLocationSummary = studyLocationService.getStudyLocationSummary(params.studyLocationName)
        def visitDetail = studyLocationService.getVisitDetails(visitId as String)
        def dataList = []
        switch (samplingUnit) {
            case "POINT_INTERCEPT":
                dataList = visitDetail.pointInterceptWithHerbIdAddedList
                break
            case "STRUCTURAL_SUMMARY":
                dataList = visitDetail.structuralSummaryList
                break
            case "SOIL_STRUCTURE":
                dataList = visitDetail.soilStructureList
                break
            case "SOIL_CHARACTER":
                dataList = visitDetail.soilCharacterisationList
                break
            case "SOIL_SAMPLING":
                dataList = visitDetail.soilSampleList
                break
            case "BASAL_AREA":
                dataList = visitDetail.basalAreaList
                break;
            default:
                break;
        }

        def colHeadings = dataList[0]?.collect { it.key }

        colHeadings?.removeAll(['siteLocationVisitId'])

        samplingUnit = WordUtils.capitalizeFully(samplingUnit.replaceAll('_', ' '))

        [visitDetail: visitDetail, studyLocationName: params.studyLocationName, studyLocationSummary: studyLocationSummary, samplingUnit: samplingUnit, columnHeadings: colHeadings, dataList: dataList]
    }

    def nextSelectedStudyLocationSummary() {
        def siteName = params.studyLocationName as String
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        def nextSiteName = siteName
        def selectedPlotNames = appState.selectedPlotNames
        def current = selectedPlotNames.find {
            it == siteName
        }

        if (current) {
            def index = selectedPlotNames?.indexOf(current)
            if (index < selectedPlotNames.size() - 1) {
                nextSiteName = selectedPlotNames[index + 1]
            }
        }

        redirect(action:'studyLocationSummary', params:[studyLocationName:nextSiteName])
    }

    def previousSelectedStudyLocationSummary() {
        def siteName = params.studyLocationName as String
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        def prevSiteName = siteName
        def selectedPlotNames = appState.selectedPlotNames
        def current = selectedPlotNames.find {
            it == siteName
        }
        if (current) {
            def index = selectedPlotNames.indexOf(current)
            if (index > 0) {
                prevSiteName = selectedPlotNames[index - 1]
            }
        }

        redirect(action:'studyLocationSummary', params:[studyLocationName:prevSiteName])
    }

}
