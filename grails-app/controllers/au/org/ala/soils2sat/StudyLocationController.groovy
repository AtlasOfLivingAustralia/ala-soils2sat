package au.org.ala.soils2sat

import ala.soils2sat.DrawingUtils
import grails.converters.JSON
import au.com.bytecode.opencsv.CSVWriter

import java.awt.BasicStroke
import java.awt.Color
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.image.BufferedImage
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
            def studyLocationTaxaList = biocacheService.getTaxaNamesForLocation(studyLocationDetails.latitude, studyLocationDetails.longitude)
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
                    def visitDetails = studyLocationService.getVisitDetails(visitId as String)
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

    def studyLocationTaxaFragment() {
        def studyLocationName = params.studyLocationName
        def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
        def ausplotsNames = studyLocationService.getVoucheredTaxaForStudyLocation(studyLocationName)
        def alaNames = biocacheService.getTaxaNamesForLocation(studyLocationDetails.latitude, studyLocationDetails.longitude)
        def weeds = biocacheService.getWeedsOfNationalSignificance()

        [studyLocationDetails: studyLocationDetails, rank: settingService.observationsRank, radius: settingService.observationRadius, alaNames: alaNames, ausplotsNames: ausplotsNames, weeds: weeds]
    }

    def studyLocationVisitSummary() {
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState

        def studyLocationVisitId = params.studyLocationVisitId

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
        def samplingUnitTypeId = params.int("samplingUnitTypeId")
        def visitDetail = studyLocationService.getVisitDetails(visitId as String)
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
                view = 'pointInterceptDetail'
                break;
            case 4:
                view = 'structuralSummaryDetail'
                break;
        }

        render(view:view, model:model)
    }

    def pointInterceptImage() {

        def visitId = params.studyLocationVisitId as String
        def data = studyLocationService.getSamplingUnitDetails(visitId, "0")

        int legendWidth = 200
        int gridSize = 600
        int gutterSize = 40

        BufferedImage image = new BufferedImage(gridSize + gutterSize * 3 + legendWidth, gridSize + gutterSize * 2, BufferedImage.TYPE_INT_RGB)

        def g = image.graphics as Graphics2D

        float[] dash1 = [ 10.0f ];
        BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 5.0f);

        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int textLeadIn = 4;
        int textPadding = 4;
        int hgt = metrics.getHeight() + textPadding;

        def computeEWLabelRectangle = { int x, int y, String label ->
            int txtWidth = metrics.stringWidth(label) + textPadding;
            if ( x > gridSize) {
                return new Rectangle(x + textLeadIn, y - (int) (hgt / 2), txtWidth, hgt)
            } else {
                return new Rectangle(x - ( textLeadIn * 2 + txtWidth), y - (int) (hgt / 2), txtWidth, hgt)
            }
        }

        def computeNSLabelRectangle = { int x, int y, String label ->
            int adv = metrics.stringWidth(label) + textPadding;
            if (y > gridSize) {
                return new Rectangle(x - (int) (adv / 2), y + textLeadIn, adv, hgt)
            } else {
                return new Rectangle(x - (int) (adv / 2), y - (textLeadIn * 2 + hgt), adv, hgt)
            }
        }

        def drawRectangle = { Rectangle rect ->
            g.setStroke(new BasicStroke())
            g.setColor(Color.white)
            g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height)
            g.setColor(Color.black)
            g.drawRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height)
        }

        try {
            // Clear the image buffer to white
            g.setColor(Color.white)
            g.fillRect(0,0,image.width,image.height);

            def vTransectSpacing = gridSize / 5
            def hTransectSpacing = gridSize / 5
            def offsetX = (int) (vTransectSpacing / 2)
            def offsetY = (int) (hTransectSpacing / 2)

            def transects = [:]
            for (int i = 1; i <= 5; ++i) {
                def EWy = (int) (( image.height - (offsetY + 1 + gutterSize)) - ((i-1) * vTransectSpacing))
                def NSx = (int) ((((i-1) * hTransectSpacing) + offsetX + gutterSize))
                transects["W${i}-E${i}"] = [x1: gutterSize, x2: gridSize + gutterSize, y1: EWy, y2: EWy, dir: 1, dx: gridSize / 100, dy: 0 ]
                transects["E${i}-W${i}"] = [x1: gutterSize, x2: gridSize + gutterSize, y1: EWy, y2: EWy, dir: -1, dx: gridSize / 100, dy: 0 ]
                transects["N${i}-S${i}"] = [x1: NSx, x2: NSx, y1: gutterSize, y2: gutterSize + gridSize , dir: 1, dx: 0, dy: gridSize / 100 ]
                transects["S${i}-N${i}"] = [x1: NSx, x2: NSx, y1: gutterSize, y2: gutterSize + gridSize, dir: -1, dx: 0, dy: gridSize / 100 ]
            }

            g.setColor(Color.black)

            transects.each { kvp ->
                def transect = kvp.value
                def name = kvp.key
                if (transect.dir ==  1) {

                    g.setStroke(dashed);
                    g.drawLine(transect.x1, transect.y1, transect.x2, transect.y2)

                    if (name.startsWith("W")) {
                        def label = name.substring(0,2)
                        def rect = computeEWLabelRectangle(transect.x1, transect.y1, label)
                        drawRectangle(rect)
                        DrawingUtils.drawString(g, g.font, label, rect, DrawingUtils.TEXT_ALIGN_CENTER)
                        label = name.substring(3)
                        rect = computeEWLabelRectangle(transect.x2, transect.y2, label)
                        drawRectangle(rect)
                        DrawingUtils.drawString(g, g.font, label, rect, DrawingUtils.TEXT_ALIGN_CENTER)
                    } else {
                        def label = name.substring(0,2)
                        def rect = computeNSLabelRectangle(transect.x1, transect.y1, label)
                        drawRectangle(rect)
                        DrawingUtils.drawString(g, g.font, label, rect, DrawingUtils.TEXT_ALIGN_CENTER)
                        label = name.substring(3)
                        rect = computeNSLabelRectangle(transect.x2, transect.y2, label)
                        drawRectangle(rect)
                        DrawingUtils.drawString(g, g.font, label, rect, DrawingUtils.TEXT_ALIGN_CENTER)
                    }

                }
            }

            def property = params.pointInterceptType ?: "herbariumDetermination"

            def colorMap = [:]
            def colors = DrawingUtils.generatePalette(20, Color.blue)
            int colorIndex = 0

            def pointSize = (int) (gridSize / 100)

            data.samplingUnitData?.each { point ->
                def transectLabel = point.transect?.replaceAll('_', '-')
                def transect  = transects[transectLabel]
                if (transect) {
                    int offset = (int) Math.round(point.pointNumber)
                    int x = 0, y = 0
                    if (transect.dir == 1) {
                        x = transect.x1 + (int) (offset * transect.dx)
                        y = transect.y1 + (int) (offset * transect.dy)
                    } else {
                        x = transect.x2 - (int) (offset * transect.dx)
                        y = transect.y2 - (int) (offset * transect.dy)
                    }

                    def value = point[property]
                    if (value) {
                        if (!colorMap.containsKey(value)) {
                            def c = colors[colorIndex++];
                            colorMap[value] = c;
                            if (colorIndex >= colors.size()) {
                                colorIndex = 0
                            }
                        }
                        g.setColor(colorMap[value])
                        g.fillOval(x - (int) (pointSize / 2),y - (int)(pointSize / 2), pointSize, pointSize);
                    }
                }
            }

            // Draw Legend.

            def x = gutterSize * 2 + gridSize + 50
            def y = gutterSize
            colorMap.each {
                g.setColor(it.value)
                g.fillOval(x - (int) (pointSize / 2),y - (int)(pointSize / 2), pointSize, pointSize);
                g.setColor(Color.black)
                def rect = new Rectangle(x + 10, y - 12, legendWidth - 60, 25)
                DrawingUtils.drawString(g, g.getFont(), it.key, rect, DrawingUtils.TEXT_ALIGN_LEFT)
                y += 30
            }

        } finally {
            g.dispose()
        }

        def outputBytes = ImageUtils.imageToBytes(image)

        response.setContentType("image/jpeg")
        response.setHeader("Content-disposition", "attachment;filename=PI${params.studyLocationVisitId}.jpg")
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
