package au.org.ala.soils2sat

import ala.soils2sat.DrawingUtils
import grails.converters.JSON
import au.com.bytecode.opencsv.CSVWriter
import net.sf.json.JSONNull

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
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
        return extractService.getLayerDataForLocations(appState.selectedPlotNames, appState.layers?.collect({ it.name }))
    }

    def compareStudyLocations = {
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
            def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocation)
//            def studyLocationTaxaList = biocacheService.getTaxaNamesForLocation(studyLocationDetails.latitude, studyLocationDetails.longitude)
            def studyLocationTaxaList = studyLocationService.getVoucheredTaxaForStudyLocation(studyLocationDetails.studyLocationName)
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

        [results:results, appState: appState, userInstance: userInstance, diffMode: params.diffMode]
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
                    def visitDetails = studyLocationService.getStudyLocationVisitDetails(visitId as String)
                    def selection = new StudyLocationVisit(studyLocationName: visitDetails.studyLocationName, studyLocationVisitId: visitId)
                    selection.save(failOnError: true)
                    appState.addToSelectedVisits(selection)
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

    def studyLocationVoucheredTaxaFragment() {
        def studyLocationName = params.studyLocationName
        if (studyLocationName) {
            def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
            def taxaList = studyLocationService.getVoucheredTaxaForStudyLocation(studyLocationName)
            def weeds = biocacheService.getWeedsOfNationalSignificance()
            def description = "Vouchered specimens recorded at ${studyLocationDetails.studyLocationName}"
            def model = [taxaList: taxaList, weeds: weeds, taxaListDescription: description]
            render(view: 'taxaListFragment', model: model)
        }
    }

    def studyLocationOccurrenceTaxaFragment() {
        def studyLocationName = params.studyLocationName
        if (studyLocationName) {
            def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
            def taxaList = biocacheService.getTaxaNamesForLocation(studyLocationDetails.latitude, studyLocationDetails.longitude)
            def weeds = biocacheService.getWeedsOfNationalSignificance()
            def description = "Taxa list derived from specimen data collected within ${settingService.observationRadius} kilometers of ${studyLocationDetails.studyLocationName}"
            def model = [taxaList: taxaList, weeds: weeds, taxaListDescription: description]
            render(view: 'taxaListFragment', model: model)
        }
    }

    def studyLocationVisitVoucheredTaxaFragment() {
        def studyLocationVisitId = params.studyLocationVisitId
        if (studyLocationVisitId) {
            def studyLocationVisitDetails = studyLocationService.getStudyLocationVisitDetails(studyLocationVisitId)
            def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationVisitDetails?.studyLocationName)
            def taxaList = studyLocationService.getVoucheredTaxaForStudyLocationVisit(studyLocationVisitId)
            def weeds = biocacheService.getWeedsOfNationalSignificance()
            def description = "Vouchered specimens recorded at ${studyLocationDetails.studyLocationName} during visit starting ${studyLocationVisitDetails.visitStartDate}"
            def model = [taxaList: taxaList, weeds: weeds, taxaListDescription: description]
            render(view: 'taxaListFragment', model: model)
        }
    }

    def studyLocationVisitOccurrenceTaxaFragment() {
        def studyLocationVisitId = params.studyLocationVisitId
        if (studyLocationVisitId) {
            def studyLocationVisitDetails = studyLocationService.getStudyLocationVisitDetails(studyLocationVisitId)
            def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationVisitDetails?.studyLocationName)
            def taxaList = biocacheService.getTaxaNamesForLocation(studyLocationDetails.latitude, studyLocationDetails.longitude)
            def weeds = biocacheService.getWeedsOfNationalSignificance()
            def description = "Taxa list derived from specimen data collected within ${settingService.observationRadius} kilometers of ${studyLocationDetails.studyLocationName} during visit starting ${studyLocationVisitDetails.visitStartDate}"
            def model = [taxaList: taxaList, weeds: weeds, taxaListDescription: description]
            render(view: 'taxaListFragment', model: model)
        }
    }

    def studyLocationVisitSummary() {
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def studyLocationVisitId = params.studyLocationVisitId

        def visitDetail = studyLocationService.getStudyLocationVisitDetails(studyLocationVisitId)
        def studyLocationName = visitDetail?.studyLocationName
        def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)

        def isSelected = appState.selectedVisits.find { it.studyLocationVisitId == studyLocationVisitId }

        [studyLocationName: studyLocationName, studyLocationDetails: studyLocationDetails, visitDetail: visitDetail, isSelected: isSelected]
    }

    def ajaxSelectedStudyLocationsFragment() {
        def userInstance = springSecurityService.currentUser as User
        [userInstance: userInstance, appState: userInstance?.applicationState]
    }

    def downloadSamplingUnit() {
        def visitId = params.studyLocationVisitId
        def samplingUnitTypeId = params.int("samplingUnitTypeId")
        def visitDetail = studyLocationService.getStudyLocationVisitDetails(visitId as String)
        def data = studyLocationService.getSamplingUnitDetails(visitId, samplingUnitTypeId?.toString());

        def dataList = data?.samplingUnitData
        def samplingUnitName = data?.samplingUnit?.description

        response.setHeader("Content-Disposition", "attachment;filename=${visitDetail.studyLocationName}_${visitDetail.visitStartDate}_${samplingUnitName}.zip")
        response.setContentType("application/zip")

        def zipStream = new ZipOutputStream(response.getOutputStream())
        try {
            OutputStream bos = new BufferedOutputStream(zipStream);
            OutputStreamWriter outputwriter = new OutputStreamWriter(bos);
            BufferedWriter writer = new BufferedWriter(outputwriter);
            zipStream.putNextEntry(new ZipEntry("${samplingUnitName}.csv"));

            Map first = dataList.first()

            def sb = new StringBuilder()
            def keySet = first.keySet().sort { it } // always get them entries in the same order
            // print header row first...
            keySet.each {
                sb.append('"').append(it).append('",')
            }
            // trim off last comma
            sb.deleteCharAt(sb.length() - 1);
            writer << sb.toString() << "\n"
            // now dump all the data...
            dataList.each { row ->
                sb = new StringBuilder()
                keySet.each { String fieldName ->
                    def element = row[fieldName]
                    if (element == null || element == JSONNull) {
                        element = ""
                    }
                    sb.append('"').append(element).append('",')
                }
                // trim off last comma
                sb.deleteCharAt(sb.length() - 1);
                writer << sb << "\n"
            }

            writer.flush()
            zipStream.closeEntry();
        } finally {
            // clean up
            zipStream.close();
        }
    }

    def samplingUnitDetail() {

        def visitId = params.studyLocationVisitId
        def samplingUnitTypeId = params.int("samplingUnitTypeId")
        def visitDetail = studyLocationService.getStudyLocationVisitDetails(visitId as String)
        def studyLocationDetail = studyLocationService.getStudyLocationDetails(visitDetail?.studyLocationName)
        def data = studyLocationService.getSamplingUnitDetails(visitId, samplingUnitTypeId?.toString());
        def dataList = data?.samplingUnitData
        def samplingUnitName = data?.samplingUnit?.description

        def colHeadings = []
        if (dataList && dataList[0]) {
            colHeadings = dataList[0]?.collect { it.key }
        }

        colHeadings?.remove('id')

        def model = [visitDetail: visitDetail, studyLocationDetail: studyLocationDetail, samplingUnitTypeId: samplingUnitTypeId, samplingUnitName: samplingUnitName, columnHeadings: colHeadings, dataList: dataList]

        def view = 'samplingUnitDetail'
        switch (samplingUnitTypeId) {
            case 0:
                dataList = dataList.sort { it.transect }
                def interceptTypesList = ['substrate', 'herbariumDetermination', 'growthForm']
                model.interceptTypes = interceptTypesList.collectEntries { [it, StringUtils.makeTitleFromCamelCase(it)]}
                view = 'pointInterceptDetail'
                break;
            case 4:
                view = 'structuralSummaryDetail'
                break;
            case 7:
                def groups = [
                        "Miscellaneous" : ["comments", "ec", "ph", "effervescence", "collectedBy", "horizon"],
//                        "Depth" :["layerNumber", "upperDepth", "lowerDepth"],
                        "Texture" : ["textureGrade", "textureModifier", "textureQualifier"],
                        "Colour" : ["colourWhenDry", "colourWhenMoist"],
                        "Mottles" : ["mottlesColour", "mottlesAbundance"],
                        "Coarse Fragments": ["coarseFragmentsSize", "coarseFragmentsShape", "coarseFragmentsAbundance", "coarseFragmentsLithology"],
                        "Segregation" : ["segregationForm", "segregationNature", "segregationSize", "segregationsAbundance"],
                        "Structure" : ["smallestSize1", "smallestSize2", "smallestSizeType1","smallestSizeType2","nextSize1","nextSize2", "nextSizeType1", "nextSizeType2"],
                        "Pedality" : ["pedalityFabric", "pedalityGrade", "pedalityType"]

                ]
                dataList = dataList.sort { it.upperDepth }
                model['groups'] = groups
                view = 'soilCharacterDetail'
                break;
        }

        render(view:view, model:model)
    }

    def pointInterceptVisualisations() {
        def visitId = params.studyLocationVisitId as String
        def data = studyLocationService.getSamplingUnitDetails(visitId, SamplingUnitType.POINT_INTERCEPT)
        def pointInterceptType = params.pointInterceptType ?: "herbariumDetermination"

        if (!data) {
            return
        }
        [studyLocationVisitId: visitId, data: data?.samplingUnitData, pointInterceptType: pointInterceptType]
    }

    def pointInterceptImage() {

        def visitId = params.studyLocationVisitId as String
        def data = studyLocationService.getSamplingUnitDetails(visitId, SamplingUnitType.POINT_INTERCEPT)

        if (!data) {
            return
        }

        TransectImageDescriptor plot = VisualisationUtils.drawPlotTransects(600, 40, 200)

        def g = plot.image.graphics as Graphics2D
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        def property = params.pointInterceptType ?: "herbariumDetermination"

        try {
            // This pre-fills the color map with a color palette (if defined), otherwise returns an empty map to be filled by the random color generator
            Map<String, Color> colorMap = VisualisationUtils.getColorMapForIntersectProperty(property)
            // A list of random colors to use if no color is predefined for a particular field value
            List<Color> colors = DrawingUtils.generatePalette(20, Color.blue)
            int colorIndex = 0

            def pointSizeMultiplierMap = VisualisationUtils.getPointSizeMultiplierMapForIntersectProperty(property)
            // If there are entries to resize points based on value, we need to sort the point intercept rows by the size of each point so that
            // bigger points are drawn first, followed by the smaller ones (so big points don't hide small ones)
            def rows = data.samplingUnitData
            if (pointSizeMultiplierMap) {
                rows = rows?.sort { 1 / (pointSizeMultiplierMap[it[property]] ?: 1.0) } // invert so sorts descending
            }

            def pointSize = (int) (plot.gridSize / 100) // Base point size - the point size may be affected by some other modifier depending on property and value

            rows?.each { point ->
                def transectLabel = point.transect?.replaceAll('_', '-')
                def transect  = plot.transects[transectLabel]
                if (transect) {
                    double offset = (double) point.pointNumber
                    if (offset >= 0 && offset <= 100) {
                        int x = 0, y = 0
                        if (transect.direction == 1) {
                            x = transect.x1 + (int) (offset * transect.dx)
                            y = transect.y1 + (int) (offset * transect.dy)
                        } else {
                            x = transect.x2 - (int) (offset * transect.dx)
                            y = transect.y2 - (int) (offset * transect.dy)
                        }

                        def value = point[property] as String
                        if (value) {
                            if (!colorMap.containsKey(value)) {
                                def c = colors[colorIndex++];
                                colorMap[value] = c;
                                if (colorIndex >= colors.size()) {
                                    colorIndex = 0
                                }
                            }
                            g.setColor(colorMap[value])
                            def finalPointSize = (int) (pointSize * (pointSizeMultiplierMap[value] ?: 1.0))

                            g.fillOval(x - (int) (finalPointSize / 2),y - (int)(finalPointSize / 2), finalPointSize, finalPointSize);
                        }
                    }
                }
            }

            // Draw Legend.

            def x = plot.gutterSize * 2 + plot.gridSize + 50
            def y = plot.gutterSize
            def keys = colorMap.keySet().sort { pointSizeMultiplierMap[it] ?: 1 }

            keys.each {
                def color = colorMap[it]
                g.setColor(color)
                def finalPointSize = (int) (pointSize * (pointSizeMultiplierMap[it] ?: 1.0))
                g.fillOval(x - (int) (finalPointSize / 2),y - (int)(finalPointSize / 2), finalPointSize, finalPointSize);
                g.setColor(Color.black)
                def rect = new Rectangle(x + 14, y - 12, plot.legendWidth - 60, 25)
                DrawingUtils.drawString(g, g.getFont(), it, rect, DrawingUtils.TEXT_ALIGN_LEFT)
                y += 30
            }

        } finally {
            g.dispose()
        }

        def outputBytes = ImageUtils.imageToBytes(plot.image, "PNG")

        response.setContentType("image/png")
        response.setHeader("Content-disposition", "attachment;filename=PI${params.studyLocationVisitId}_${property}.png")
        response.outputStream.write(outputBytes)
        response.flushBuffer()

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

    def studyLocationSamplingUnitDetails() {

        def visits = studyLocationService.getStudyLocationVisits(params.studyLocationName)
        if (visits) {
            redirect(controller:'studyLocation', action:'samplingUnitDetail', params:[studyLocationVisitId: visits[0].studyLocationVisitId, samplingUnitTypeId: params.samplingUnitTypeId])
            return
        }
        redirect(action:'studyLocationSummary', params:[studyLocationName: params.studyLocationName])
    }

}
