/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.soils2sat

import ala.soils2sat.CodeTimer
import ala.soils2sat.DrawingUtils

import java.awt.Color

class VisualisationController {

    def studyLocationService
    def biocacheService
    def springSecurityService
    def layerService
    def extractService

    def studyLocationVisitVisualisations() {
        def studyLocationVisitDetails = studyLocationService.getStudyLocationVisitDetails(params.studyLocationVisitId)
        [studyLocationVisitDetails: studyLocationVisitDetails]
    }

    def structuralSummaryForVisit() {

        def columns = [['string', 'Species'], ['number', 'U1'], ['number', 'U2'], ['number', 'U3'],['number', 'M1'],['number', 'M2'],['number', 'M3'],['number', 'G1'],['number', 'G2'],['number', 'G3']]
        // def colors = ['#4E6228','#652524', '#4E81BD']
        def colors = VisualisationUtils.structualSummaryColors
        def taxaMap = studyLocationService.getPointInterceptTaxaForVisit(params.studyLocationVisitId)
        def data = []
        if (taxaMap) {
            def structuralSummary = studyLocationService.getSamplingUnitDetails(params.studyLocationVisitId, SamplingUnitType.StructuralSummary)?.samplingUnitData[0]

            // Cleanse duplicate spaces out of taxa names
            structuralSummary.each {
                if (it.key?.contains("Dominant")) {
                    it.value = StringUtils.collapseSpaces(it.value)
                }
            }


            def colNames = ['upper1Dominant','upper2Dominant','upper3Dominant','mid1Dominant','mid2Dominant','mid3Dominant','ground1Dominant','ground2Dominant','ground3Dominant']
            int i = 0;
            colNames.each {
                def row = [ "${it.substring(0,1).toUpperCase()}${(i % 3)+1} ${structuralSummary[it] ?: ''}"]
                for (int j = 0; j < colNames.size(); ++j) {
                    if (j == i) {
                        row << (taxaMap[structuralSummary[it]] ?: 0)
                    } else {
                        row << 0
                    }
                }
                i++
                data << row
            }

            // check to see that we actually have some non-zero data...
            if (isEmptyChartData(data)) {
                data = []
            }
        }

        render(view:'columnChart', model: [columns: columns, data: data, colors: colors, title: "Structural Summary", name:'structuralSummary', stacked: true, vAxisTitle:'Count of Species', hAxisTitle:'Stratum Type'])
    }

    def pointInterceptTaxaForVisit() {
        def columns = [['string', 'Species'], ['number', 'Count']]
        def taxaMap = studyLocationService.getPointInterceptTaxaForVisit(params.studyLocationVisitId)
        def data = []
        if (taxaMap) {
            // sort by inverted value so order is descending
            def names = taxaMap.sort { (it.value ? (1 / it.value) : 0) }.keySet()
            names.each {
                data << [it, taxaMap[it] ?: 0]
            }
            // check to see that we actually have some non-zero data...
            if (isEmptyChartData(data)) {
                data = []
            }
        }

        render(view:'columnChart', model: [columns: columns, data: data, colors: ['goldenrod'], title: "Point Intercept Taxa", name:'PITaxa', stacked: false, vAxisTitle:'Number of Specimens', hAxisTitle:'Species'])
    }

    def soilECForVisit() {
        def colors = [ '#4E81BD' ]
        def columns = [
                ['string',"Depth"],
                ['number', "Soil EC"]
        ]

        def samplingUnitData = studyLocationService.getSoilECForStudyLocationVisit(params.studyLocationVisitId)
        def data = samplingUnitData?.collect { ["${it.upperDepth} - ${it.lowerDepth}", StringUtils.firstNumber(it.EC?.toString()) ]}

        // is there at least one row with non-null data?
        if (isEmptyChartData(data)) {
            data = []
        }

        return render(view:'barChart', model: [columns: columns, name:'soilECForVisit', title:'Soil EC', data: data, colors: colors, vAxisTitle:'Soil Depth (Metres)', hAxisTitle:'Electrical Conductivity'])
    }

    def soilpHForVisit() {

        def litmusColors = VisualisationUtils.getLitmusColors()
        def colors = litmusColors.collect { it.color }
        def columns = [["string","Depth"]]
        litmusColors.each {
            columns << ['number', "pH ${it.pH}"]
        }

        def realData = studyLocationService.getSoilPhForStudyLocationVisit(params.studyLocationVisitId)

        def data = realData?.collect { [depth: "${it.upperDepth} - ${it.lowerDepth}", ph: it.pH ]}

        def adjustedData = []

        // is there at least one row with non-null data?
        def nonNull = data.find { it.ph != null }
        if (!nonNull) {
            data = []
        }

        data?.each { element ->
            def row = [element.depth]
            boolean found = false
            def ph = element.ph ?: 0
            litmusColors.each { color ->
                if (!found && color.pH > ph) {
                    found = true;
                    if (row.size() > 1) {
                        row.pop()
                    }
                    row << ph
                    row << 0
                } else {
                    row << 0
                }
            }
            adjustedData << row
        }

        render(view:'barChart', model:[name:'soilPhForLocation', title:'Soil pH', columns: columns, data: adjustedData, colors: colors, vAxisTitle:'Soil Depth (Metres)', hAxisTitle:'pH' ])

    }

    private isEmptyChartData(List data) {
        // first element of every row is the label, so check every other element, and make sure there is at least one non-zero element

        for (int j =0; j < data.size(); ++j) {
            def row = data[j]
            if (row?.size() > 0) {
                for (int i = 1; i < row.size(); ++i) {
                    if (row[i]) {
                        return false
                    }
                }
            }
        }
        return true
    }

    public weedNonWeedBreakdownForVisit() {

        def weedList = biocacheService.getWeedsOfNationalSignificance()?.sort { it }

        def data = []
        def colors = ['#99B958', '#BD4E4C']
        def columns = [
            ['string', 'Label'], ['number', 'abundance']
        ]

        def ausplotsNames = studyLocationService.getVoucheredTaxaForStudyLocationVisit(params.studyLocationVisitId)

        if (ausplotsNames) {
            def weedCount = 0
            weedList.each { weedName ->
                def weed = ausplotsNames.find { it.trim()?.equalsIgnoreCase(weedName.trim()) }
                if (weed) {
                    weedCount++
                }
            }

            data <<  ['Non-Weed species', ausplotsNames.size() - weedCount]
            data << ['Weed species', weedCount]
      }

        render(view:'pieChart', model:[name:'weedNonWeedBreakdownForLocation', title:'Weed / Non-Weed Species Abundance % Breakdown', columns: columns, data: data, colors: colors])
    }

    def studyLocationVisualisations() {

        [studyLocationName: params.studyLocationName]
    }

    def compareStudyLocationVisualisations() {

    }

    def compareLandformElement() {
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        def columns = [['string', 'Study Location'], ['number', 'Landform Element']]

        def elementMap = [:]
        appState.selectedPlotNames.each { studyLocationName ->
            def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
            def landformElement = studyLocationDetails.landformElement ?: 'Unspecified'
            if (elementMap.containsKey(landformElement)) {
                elementMap[landformElement]++
            } else {
                elementMap[landformElement] = 1;
            }
        }

        def data = []
        elementMap.keySet().each {
            data << [it, elementMap[it]]
        }

        render(view: 'pieChart', model: [columns: columns, data: data, name:'compareLandformElement', title:'Study Location & Breakdown by Landform Element'])
    }

    def compareScalarLayer() {
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        def layerName = params.layerName
        def layerInfo = layerService.getLayerInfo(layerName)

        def title = layerInfo.displayname
        if (layerInfo.environmentalvalueunits) {
            title += " (${layerInfo.environmentalvalueunits})"
        }

        def columns = [['string', 'Study Location'], ['number', layerInfo.displayname]]
        def data = []

        def selectedPlotNames = appState.selectedPlotNames

        if (params.studyLocationNames) {
            selectedPlotNames = params.studyLocationNames?.split(",")
        }

        selectedPlotNames?.each { studyLocationName ->
            def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
            def values = layerService.getIntersectValues(studyLocationDetails.latitude, studyLocationDetails.longitude, [layerName])
            data << [studyLocationName, values[layerName] ?: 0]
        }

        def colors = [ '#4E81BD' ]

        if (params.color) {
            colors = [params.color]
        }

        return [columns: columns, data: data, colors: colors, layerInfo: layerInfo, title: title]
    }

    def compareDistinctSpecies() {
        def data = []
        def userInstance = springSecurityService.currentUser as User
        def appState = userInstance?.applicationState
        def colors = ['#99B958']
        def columns = [['string', 'Study Location'], ['number', 'Number of distinct species']]
        def nameMap = [:]
        appState.selectedPlotNames.each {
            nameMap[it] = studyLocationService.getVoucheredTaxaForStudyLocation(it)
        }

        def distinctMap = [:]
        appState.selectedPlotNames?.each { studyLocation ->
            def newList = []
            def candidateList = nameMap[studyLocation]
            candidateList.each { taxa ->
                def include = true
                nameMap.each { kvp ->
                    if (include && kvp.key != studyLocation) {
                        if (kvp.value.contains(taxa)) {
                            include = false
                        }
                    }
                }
                if (include) {
                    newList << taxa
                }
            }
            distinctMap[studyLocation] = newList
        }

        appState.selectedPlotNames.each {
            data << [it, distinctMap[it]?.size()]
        }

        render(view:'columnChart', model: [columns: columns, data: data, colors: colors, title: "Number of Unique Species for AusPlots / TREND", name:'compareDistinctSpecies', stacked: false, hAxisTitle: 'Study Location', vAxisTitle: 'Number of Distinct Species'])
    }

    def plantSpeciesBreakdownBySource() {

        def columns = [['string',"Label"],['number', "%"]]

        def ausplotsNames = studyLocationService.getVoucheredTaxaForStudyLocation(params.studyLocationName)
        def studyLocationDetails = studyLocationService.getStudyLocationDetails(params.studyLocationName)
        def alaNames = []
        if (studyLocationDetails) {
            alaNames = biocacheService.getTaxaNamesForLocation(studyLocationDetails?.latitude, studyLocationDetails?.longitude)
        }

        def both = []
        alaNames.each {
            if (ausplotsNames.contains(it)) {
                both.add(it)
            }
        }

        both.each {
            ausplotsNames.remove(it)
            alaNames.remove(it)
        }

        def data = [
            ["Both AusPlots & ALA", both.size()],
            ["AusPlots only", ausplotsNames.size()],
            ["ALA Only", alaNames.size()]
        ]

        def colors = ['#99B958', '#4E80BB', '#BD4E4C']

        def modalContentLink = createLink(action:'breakdownSpeciesList', params:[studyLocationName: params.studyLocationName])

        render(view:'pieChart', model: [columns: columns, name: 'speciesBreakdownBySource', data: data, colors: colors, title:"Plant Species Breakdown by Source", selectHandler:'plantSpeciesBreakDownBySourceClick', modalContentLink: modalContentLink])
    }

    def breakdownSpeciesList() {
        def row = params.int("row")

        def ausplotsNames = studyLocationService.getVoucheredTaxaForStudyLocation(params.studyLocationName)
        def studyLocationDetails = studyLocationService.getStudyLocationDetails(params.studyLocationName)
        def alaNames = biocacheService.getTaxaNamesForLocation(studyLocationDetails.latitude, studyLocationDetails.longitude)
        def both = []
        alaNames.each {
            if (ausplotsNames.contains(it)) {
                both.add(it)
            }
        }

        both.each {
            ausplotsNames.remove(it)
            alaNames.remove(it)
        }


        def title = "No Title"
        def description = "No Description"
        def nameList = []
        switch (row) {
            case 0:
                title = "Both AusPlots and ALA"
                description = "These names occur both as AusPlots vouchered specimens from this location, as well as observational data sourced from the Atlas of Living Australia"
                nameList = both
                break;
            case 1:
                title = "AusPlots only"
                description = "These names occur only as AusPlots vouchered specimens from this location"
                nameList = ausplotsNames
                break;
            case 2:
                title = "ALA only"
                description = "These names occur only in observation records sourced from the Atlas of Living Australia around this area"
                nameList = alaNames
                break;
            default:
            break;
        }

        [title: title, description: description, nameList: nameList]
    }

    def soilECForLocation() {

        def colors = [ '#4E81BD' ]
        def columns = [
            ['string',"Depth"],
            ['number', "Soil EC"]
        ]

        def samplingUnitData = studyLocationService.getSoilECForStudyLocation(params.studyLocationName)
        def data = samplingUnitData?.collect { ["${it.upperDepth} - ${it.lowerDepth}", StringUtils.firstNumber(it.EC?.toString()) ]}

        // is there at least one row with non-null data?
        if (isEmptyChartData(data)) {
            data = []
        }

        return render(view:'barChart', model: [columns: columns, name:'soilECForLocation', title:'Soil EC', data: data, colors: colors, vAxisTitle:'Soil Depth (Metres)', hAxisTitle:'Electrical Conductivity'])
    }

    def soilpHForLocation() {

        def litmusColors = VisualisationUtils.getLitmusColors()

        def columns = [['string',"Depth"]]
        litmusColors.each {
            columns << ['number', "pH ${it.pH}"]
        }

        def realData = studyLocationService.getSoilPhForStudyLocation(params.studyLocationName)

        def data = realData?.collect { [depth: "${it.upperDepth} - ${it.lowerDepth}", ph: it.pH]}

        def adjustedData = []

        // is there at least one row with non-null data?
        def nonNull = data.find { it.ph != null }
        if (!nonNull) {
            data = []
        }

        data?.each { element ->
            def row = [element.depth]
            boolean found = false
            def ph = element.ph ?: 0
            litmusColors.each { color ->
                if (!found && color.pH > ph) {
                    found = true;
                    if (row.size() > 1) {
                        row.pop()
                    }
                    row << ph
                    row << 0
                } else {
                    row << 0
                }
            }
            adjustedData << row
        }

        def colors = litmusColors.collect { it.color }

        render(view:'barChart', model:[name:'soilPhForLocation', title:'Soil pH', columns: columns, data: adjustedData, colors: colors, vAxisTitle:'Soil Depth (Metres)', hAxisTitle:'pH'])
    }

    public weedNonWeedBreakdownForLocation() {

        def weedList = biocacheService.getWeedsOfNationalSignificance()?.sort { it }

        def ausplotsNames = studyLocationService.getVoucheredTaxaForStudyLocation(params.studyLocationName)

        def weedCount = 0
        weedList.each { weedName ->
            def weed = ausplotsNames.find { it.trim()?.equalsIgnoreCase(weedName.trim()) }
            if (weed) {
                weedCount++
            }
        }


        def columns = [
            ['string', 'Label'], ['number', 'abundance']
        ]

        def data = [
            ['Non-Weed species', ausplotsNames.size() - weedCount],
            ['Weed species', weedCount]
        ]

        def colors = ['#99B958', '#BD4E4C']

        render(view:'pieChart', model:[name:'weedNonWeedBreakdownForLocation', title:'Weed / Non-Weed Species Abundance % Breakdown', columns: columns, data: data, colors: colors])
    }

    def speciesAnalysisFlow = {

        initialize {

            action {
                def user = springSecurityService.currentUser as User
                def appState = user.applicationState
                def selectedVisitIds = []

                appState?.selectedVisits?.each {
                    selectedVisitIds << it.studyLocationVisitId
                }

                def selectedLayers = []
                appState?.layers?.each {
                    selectedLayers << it.name
                }

                flow.selectedVisitIds = selectedVisitIds
                flow.selectedLayers = selectedLayers
                flow.speciesName = params.speciesName
                flow.flowTitle = "Species Analysis <i>${params.speciesName}</i>"

                [appState: appState, user: user]
            }

            on("success").to "showVisits"
        }

        showVisits {

            on("continue") {
                def selected = params.get("visitId")
                def selectedVisitIds = []
                if (!selected) {
                    flow.selectedVisitIds = selectedVisitIds
                    flash.errorMessage = "You must select at least one visit!"
                    return error()
                }

                if (selected instanceof String) {
                    selectedVisitIds << selected
                } else if (isCollection(selected)) {
                    selected.each {
                        selectedVisitIds << it
                    }
                }
                flow.selectedVisitIds = selectedVisitIds
            }.to "showLayers"

            on("cancel").to "cancel"
        }

        showLayers {
            on("continue") {
                def selected = params.get("layerName")
                def selectedLayers = []
                if (!selected) {
                    flow.selectedLayers = selectedLayers
                    flash.errorMessage = "You must select at least one layer"
                    return error()
                }

                if (selected instanceof String) {
                    selectedLayers << selected
                } else if (isCollection(selected)) {
                    selected.each {
                        selectedLayers << it
                    }
                }
                flow.selectedLayers = selectedLayers

            }.to "showVisualisations"
            on("cancel").to "cancel"
            on("back").to "showVisits"
        }

        showVisualisations {

            onEntry {
                // Sort study locations by latitude (as a surrogate for climate)
                def locations = []
                flow.selectedVisitIds?.each {
                    def studyLocationName = studyLocationService.getStudyLocationNameForVisitId(it)
                    locations << studyLocationService.getStudyLocationDetails(studyLocationName)
                }
                locations = locations.sort { it.latitude }
                flow.studyLocationNames = locations.collect { it.studyLocationName }

                def layerData = extractService.getLayerDataForLocations(flow.studyLocationNames, flow.selectedLayers)
                flow.layerData = layerData

            }

            on("back").to "showLayers"
            on("finish").to "finish"
        }

        cancel {
            redirect(controller:'map', action:"index")
        }

        finish {
            redirect(controller:'map', action:"index")
        }

    }

    def speciesAnalysisPointInterceptCounts() {
        def columns = [['string', 'StudyLocation'], ['number', 'Count']]
        def colors = ['#9ABB59']
        def data = []
        def speciesName = params.speciesName
        params.studyLocationNames?.split(",")?.each {
            def value = 0
            def visit = studyLocationService.getLastVisitForStudyLocation(it)
            if (visit) {
                def piData = studyLocationService.getPointInterceptTaxaForVisit(visit.studyLocationVisitId)
                value = piData[speciesName]
            }
            data << [it, value]
        }

        // check to see that we actually have some non-zero data...
        if (isEmptyChartData(data)) {
            data = []
        }

        render(view:'columnChart', model: [columns: columns, data: data, colors: colors, title: "Point Intercept Counts at last visit", name:'specAnPIC', stacked: false, vAxisTitle:'Count', hAxisTitle:'Study Location'])
    }

    def speciesAnalysisSoilPh() {
        def columns = [['string', 'StudyLocation'], ['number', 'Count']]
        def colors = ['#616161']
        def data = []
        def speciesName = params.speciesName
        params.studyLocationNames?.split(",")?.each {
            def value = 0
            def visit = studyLocationService.getLastVisitForStudyLocation(it)
            if (visit) {
                def phData = studyLocationService.getSoilPhForStudyLocationVisit(visit.studyLocationVisitId)

                double total = 0
                if (phData) {
                    phData.each {
                        if (it.pH) {
                            total += (double) it.pH
                        }
                    }
                    value = total / phData.size()
                }
            }
            data << [it, value]
        }

        // check to see that we actually have some non-zero data...
        if (isEmptyChartData(data)) {
            data = []
        }

        render(view:'columnChart', model: [columns: columns, data: data, colors: colors, title: "Avg Soil pH (AusPlots data)", name:'specAnPH', stacked: false, vAxisTitle:'pH', hAxisTitle:'Study Location'])
    }

    def speciesAnalysisLatitude() {
        def columns = [['string', 'StudyLocation'], ['number', 'Latitude']]
        def colors = ['blue']
        def data = []

        params.studyLocationNames?.split(",")?.each {
            def value = 0
            def location = studyLocationService.getStudyLocationDetails(it)
            if (location) {
                value = location.latitude
            }
            data << [it, value]
        }

        // check to see that we actually have some non-zero data...
        if (isEmptyChartData(data)) {
            data = []
        }

        render(view:'columnChart', model: [columns: columns, data: data, colors: colors, title: "Study Location Latitude", name:'specAnLatitude', stacked: false, vAxisTitle:'Latitude', hAxisTitle:'Study Location'])
    }

    def PICountComparison() {
        def pointInterceptType = params.pointInterceptType as String
        def colorMap = VisualisationUtils.getColorMapForIntersectProperty(pointInterceptType)

        def samplingUnit = studyLocationService.getSamplingUnitDetails(params.studyLocationVisitId, SamplingUnitType.PointIntercept)
        if (!samplingUnit) {
            return
        }

        List<Color> palette = DrawingUtils.generatePalette(20, Color.blue)
        int colorIdx = 0

        def dataList = samplingUnit.samplingUnitData
        // now split out into a map of property value to count...
        def counts = dataList.countBy { it[pointInterceptType] }
        def data = []
        def colors = []

        // columns first...
        def title = StringUtils.makeTitleFromCamelCase(pointInterceptType)
        def columns = [['string', title]]

        counts.keySet().each {
            if (it) {
                columns << ['number', it]
            }
        }

        counts.keySet().each { property ->
            if (property) {
                def row = [property]
                counts.keySet().each {
                    if (it) {
                        if (property == it) {
                            row << counts[property]
                        } else {
                            row << 0
                        }
                    }
                }

                data << row

                if (colorMap) {
                    colors << "#" + Integer.toHexString(colorMap[property]?.getRGB()).substring(2)
                } else {
                    colors << "#" + Integer.toHexString(palette[colorIdx]?.getRGB()).substring(2)
                    if (++colorIdx >= palette.size()) {
                        colorIdx = 0
                    }
                }
            }
        }

        // check to see that we actually have some non-zero data...
        if (isEmptyChartData(data)) {
            data = []
        }

        def chartTitle = "PI Counts Comparison for " + title

        render(view:'columnChart', model: [columns: columns, data: data, colors: colors, title: chartTitle , name:'PICountsComparison', stacked: true, vAxisTitle:'Count', hAxisTitle:title])
    }

    public PICountPercentBreakdown() {

        def pointInterceptType = params.pointInterceptType as String
        def colorMap = VisualisationUtils.getColorMapForIntersectProperty(pointInterceptType)

        def samplingUnit = studyLocationService.getSamplingUnitDetails(params.studyLocationVisitId, SamplingUnitType.PointIntercept)
        if (!samplingUnit) {
            return
        }

        def title = StringUtils.makeTitleFromCamelCase(pointInterceptType)
        def columns = [['string', title], ['number', 'Count']]


        List<Color> palette = DrawingUtils.generatePalette(20, Color.blue)
        int colorIdx = 0

        def dataList = samplingUnit.samplingUnitData
        // now split out into a map of property value to count...
        def counts = dataList.countBy { it[pointInterceptType] }
        def data = []
        def colors = []

        counts.keySet().each { property ->
            if (property) {
                data << [property, counts[property]]
                if (colorMap) {
                    colors << "#" + Integer.toHexString(colorMap[property]?.getRGB()).substring(2)
                } else {
                    colors << "#" + Integer.toHexString(palette[colorIdx]?.getRGB()).substring(2)
                    if (++colorIdx >= palette.size()) {
                        colorIdx = 0
                    }
                }
            }
        }

        // check to see that we actually have some non-zero data...
        if (isEmptyChartData(data)) {
            data = []
        }

        render(view:'pieChart', model:[name:'PICountBreakDown', title:'% Breakdown for ' + title, columns: columns, data: data, colors: colors])
    }

    def AusCoverFractionalCover() {

        def ausCoverData = getAusCoverFractionalCover(params.studyLocationName)

        def columns = [['string', "Percent"], ['number', 'Count']]
        def data =[
                ["Photosynthetic Vegetation (PV)", ausCoverData[FractionalCoverState.PV]],
                ["Non-Photosynthetic Vegetation (NPV)", ausCoverData[FractionalCoverState.NPV]],
                ["Bare Soil (BS)", ausCoverData[FractionalCoverState.BS]]
        ]

        def colors = ['green', '#FA9D03', '#71381E']

        render(view:'pieChart', model:[name:'AusCoverFractionalCover', title:'Fractional Cover (AusCover)', columns: columns, data: data, colors: colors])
    }

    def PIFractionalCover() {
        def piData = studyLocationService.getSamplingUnitDetails(params.studyLocationVisitId as String, SamplingUnitType.PointIntercept)
        def results = [:]
        if (piData) {
            results = calculateFractionalCover(piData.samplingUnitData)
        }

        def columns = [['string', "Percent"], ['number', 'Count']]
        def data =[
                ["Photosynthetic Vegetation (PV)", results[FractionalCoverState.PV] ?: 0],
                ["Non-Photosynthetic Vegetation (NPV)", results[FractionalCoverState.NPV] ?: 0],
                ["Bare Soil (BS)", results[FractionalCoverState.BS] ?: 0]
        ]

        def colors = ['green', '#FA9D03', '#71381E']

        render(view:'pieChart', model:[name:'PIFractionalCover', title:'Fractional Cover (AusPlots)', columns: columns, data: data, colors: colors])
    }

    def PIGroundCover() {
        def piData = studyLocationService.getSamplingUnitDetails(params.studyLocationVisitId as String, SamplingUnitType.PointIntercept)
        def results = [:]
        if (piData) {
            results = calculateFractionalCover(piData.samplingUnitData, ['chenopod', 'epiphyte', 'nc', 'shrub', 'tree mallee', 'tree/palm', ''])
        }

        def columns = [['string', "Percent"], ['number', 'Count']]
        def data =[
                ["Photosynthetic Vegetation (PV)", results[FractionalCoverState.PV] ?: 0],
                ["Non-Photosynthetic Vegetation (NPV)", results[FractionalCoverState.NPV] ?: 0],
                ["Bare Soil (BS)", results[FractionalCoverState.BS] ?: 0]
        ]

        def colors = ['green', '#FA9D03', '#71381E']

        render(view:'pieChart', model:[name:'PIGroundCover', title:'Fractional Ground Cover (AusPlots)', columns: columns, data: data, colors: colors])
    }

    private static def calculateFractionalCover(piData, List excludedGrowthForms = null) {

        def timer = new CodeTimer("calculateFractionalCover")

        def sorted = piData.sort { a, b ->
            a.transect <=> b.transect ?: a.pointNumber <=> b.pointNumber ?: b.height <=> a.height
        }

        def pointMap = [:]

        // First pass throws out invalid rows, and sorts them into buckets based on transect and point number
        piData.each { row ->
            if (row.transect && row.pointNumber != null) {
                def key = "${row.transect}_${row.pointNumber}"
                if (!pointMap[key]) {
                    pointMap[key] = []
                }
                pointMap[key] << row
            }
        }

        def results = [:]
        FractionalCoverState.values().each {
            results[it] = 0
        }

        // second pass means going over every point that has valid data and working out what it's classification is
        pointMap.values().each { List set ->
            def classification = classifyFractionalCover(set, excludedGrowthForms)
            results[classification]++
        }

        timer.stop(true)

        return results
    }

    private static FractionalCoverState classifyFractionalCover(List rows, List excludedGrowthForms = null) {
        int i = 0;

        def result = FractionalCoverState.Ignore
        while (i < rows.size()) {
            def row = rows[i]
            if (row.inCanopySky == null && row.dead == null) {

                if (row.substrate.equalsIgnoreCase("litter") || row.substrate.equalsIgnoreCase("cwd")) {
                    return FractionalCoverState.NPV
                }

                return FractionalCoverState.BS
            }

            if ((row.inCanopySky == null || row.dead == null)) {
                // invalid row, ignore
                continue
            }

            if (row.inCanopySky || excludedGrowthForms?.contains(row.growthForm?.toLowerCase())) {
                // assume BS unless another row in this set satisfies the conditions
                if (row.substrate.equalsIgnoreCase("litter") || row.substrate.equalsIgnoreCase("cwd")) {
                    result = FractionalCoverState.NPV
                } else {
                    result = FractionalCoverState.BS
                }
            } else {
                if (row.dead) {
                    return FractionalCoverState.NPV
                } else {
                    return FractionalCoverState.PV
                }
            }

            i++
        }

        return result
    }

    private getAusCoverFractionalCover(String studyLocationName) {
        def bsLayerName = "auscover_fractional_cover_bs"
        def npvLayerName = "auscover_fractional_cover_npv"
        def pvLayerName = "auscover_fractional_cover_pv"

        def results = [:]
        FractionalCoverState.values().each {
            results[it] = 0
        }

        def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
        if (studyLocationDetails) {
            def values = layerService.getIntersectValues(studyLocationDetails.latitude, studyLocationDetails.longitude, [bsLayerName, pvLayerName, npvLayerName])
            results[FractionalCoverState.BS] = values[bsLayerName]
            results[FractionalCoverState.PV] = values[pvLayerName]
            results[FractionalCoverState.NPV] = values[npvLayerName]
        }

        return results
    }

    def PIFractionalCoverAusPlotsVsAusCover() {

        def visitDetails = studyLocationService.getStudyLocationVisitDetails(params.studyLocationVisitId)
        def ausCoverData = getAusCoverFractionalCover(visitDetails.studyLocationName)
        def piData = studyLocationService.getSamplingUnitDetails(params.studyLocationVisitId as String, SamplingUnitType.PointIntercept)
        def ausPlotsData = [:]
        if (piData) {
            ausPlotsData = calculateFractionalCover(piData.samplingUnitData)
        }

        if (ausPlotsData.containsKey(FractionalCoverState.Ignore)) {
            ausPlotsData.remove(FractionalCoverState.Ignore)
        }

        def ausPlotsTotal = 0
        ausPlotsData.values().each {
            ausPlotsTotal += it as Integer
        }

        def columns = [
            ['string', 'Classification'],
            ['number', 'AusCover'],
            ['number', 'AusPlots'],
        ]

        def data = [
            ['PV', ausCoverData[FractionalCoverState.PV], (ausPlotsData[FractionalCoverState.PV] / ausPlotsTotal) * 100],
            ['NPV', ausCoverData[FractionalCoverState.NPV], (ausPlotsData[FractionalCoverState.NPV] / ausPlotsTotal) * 100],
            ['BS', ausCoverData[FractionalCoverState.BS], ((ausPlotsData[FractionalCoverState.BS] / ausPlotsTotal) * 100)]
        ]

        def colors = ['#4E80BB', '#BD4E4C']

        render(view:'columnChart', model: [columns: columns, data: data, colors: colors, title: "AusCover vs AusPlots Fractional Cover", name:'PIFractionalCoverAusPlotsVsAusCover', stacked: false, vAxisTitle: 'Percent (%)', showLegend: true])
    }

}

enum FractionalCoverState {
    PV, NPV, BS, Ignore
}

