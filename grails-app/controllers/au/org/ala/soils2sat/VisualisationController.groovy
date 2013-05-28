package au.org.ala.soils2sat

class VisualisationController {

    def studyLocationService
    def biocacheService
    def springSecurityService
    def layerService

    def studyLocationVisitVisualisations() {
        def studyLocationVisitDetails = studyLocationService.getVisitDetails(params.studyLocationVisitId)
        [studyLocationVisitDetails: studyLocationVisitDetails]
    }

    def structuralSummaryForVisit() {

        def columns = [['string', 'Species'], ['number', 'Upper'], ['number', 'Mid'], ['number', 'Ground']]
        def colors = ['#4E6228','#652524', '#4E81BD']
        def taxaMap = studyLocationService.getPointInterceptTaxaForVisit(params.studyLocationVisitId)
        def data = []
        if (taxaMap) {
            def structuralSummary = studyLocationService.getSamplingUnitDetails(params.studyLocationVisitId, "4")?.samplingUnitData[0]

            // Cleanse duplicate spaces out of taxa names
            structuralSummary.each {
                if (it.key?.contains("Dominant")) {
                    it.value = StringUtils.collapseSpaces(it.value)
                }
            }

            data = [
                ["U1 ${structuralSummary.upper1Dominant ?:''}", taxaMap[structuralSummary.upper1Dominant] ?: 0, 0, 0],
                ["U2 ${structuralSummary.upper2Dominant ?:''}", taxaMap[structuralSummary.upper2Dominant] ?: 0, 0, 0],
                ["U3 ${structuralSummary.upper2Dominant ?:''}", taxaMap[structuralSummary.upper3Dominant] ?: 0, 0, 0],
                ["M1 ${structuralSummary.mid1Dominant ?:''}", 0, taxaMap[structuralSummary.mid1Dominant] ?: 0, 0],
                ["M2 ${structuralSummary.mid2Dominant ?:''}", 0, taxaMap[structuralSummary.mid2Dominant] ?: 0, 0],
                ["M3 ${structuralSummary.mid3Dominant ?:''}", 0, taxaMap[structuralSummary.mid3Dominant] ?: 0, 0],
                ["G1 ${structuralSummary.ground1Dominant ?:''}", 0, 0, taxaMap[structuralSummary.ground1Dominant] ?: 0],
                ["G2 ${structuralSummary.ground2Dominant ?:''}", 0, 0, taxaMap[structuralSummary.ground2Dominant] ?: 0],
                ["G3 ${structuralSummary.ground3Dominant ?:''}", 0, 0, taxaMap[structuralSummary.ground3Dominant] ?: 0],
            ]

            // check to see that we actually have some non-zero data...
            if (isEmptyChartData(data)) {
                data = []
            }
        }

        render(view:'columnChart', model: [columns: columns, data: data, colors: colors, title: "Structural Summary", name:'structuralSummary', stacked: true])
    }

    def soilECForVisit() {
        def colors = [ '#4E81BD' ]
        def columns = [
                ['string',"Depth"],
                ['number', "Soil EC"]
        ]

        def samplingUnitData = studyLocationService.getSoilECForStudyLocationVisit(params.studyLocationVisitId)
        def data = samplingUnitData?.collect { ["${it.upperDepth} - ${it.lowerDepth}", it.EC ]}

        // is there at least one row with non-null data?
        if (isEmptyChartData(data)) {
            data = []
        }

        return render(view:'barChart', model: [columns: columns, name:'soilECForVisit', title:'Soil EC', data: data, colors: colors])
    }

    def soilpHForVisit() {

        def litmusColors = getLitmusColors()
        def colors = litmusColors.collect { it.color }
        def columns = [['string',"Depth"]]
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
            litmusColors.each { color ->
                if (!found && color.pH > element.ph) {
                    found = true;
                    if (row.size() > 1) {
                        row.pop()
                    }
                    row << element.ph
                    row << 0
                } else {
                    row << 0
                }
            }
            adjustedData << row
        }

        render(view:'barChart', model:[name:'soilPhForLocation', title:'Soil pH', columns: columns, data: adjustedData, colors: colors])

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

        def taxaMap = studyLocationService.getPointInterceptTaxaForVisit(params.studyLocationVisitId)

        def data = []
        def colors = ['#99B958', '#BD4E4C']
        def columns = [
            ['string', 'Label'], ['number', 'abundance']
        ]

        if (taxaMap) {
            def ausplotsNames = taxaMap.keySet().collect()

            // def ausplotsNames = studyLocationService.getVoucheredTaxaForStudyLocation(params.studyLocationVisitId)

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

        appState.selectedPlotNames.each { studyLocationName ->
            def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
            def values = layerService.getIntersectValues(studyLocationDetails.latitude, studyLocationDetails.longitude, [layerName])
            data << [studyLocationName, values[layerName] ?: 0]
        }

        def colors = [ '#4E81BD' ]

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

        [data: data, colors: colors, columns: columns]
    }

    def plantSpeciesBreakdownBySource() {

        def columns = [['string',"Label"],['number', "%"]]

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



        def data = [
            ["Both AusPlots & ALA", both.size()],
            ["AusPlots only", ausplotsNames.size()],
            ["ALA Only", alaNames.size()]
        ]

        def colors = ['#99B958', '#4E80BB', '#BD4E4C']

        render(view:'pieChart', model: [columns: columns, name: 'speciesBreakdownBySource', data: data, colors: colors, title:"Plant Species Breakdown by Source", selectHandler:'plantSpeciesBreakDownBySourceClick'])
    }

    def soilECForLocation() {

        def colors = [ '#4E81BD' ]
        def columns = [
            ['string',"Depth"],
            ['number', "Soil EC"]
        ]

        def samplingUnitData = studyLocationService.getSoilECForStudyLocation(params.studyLocationName)
        def data = samplingUnitData?.collect { ["${it.upperDepth} - ${it.lowerDepth}", it.EC ]}

        // is there at least one row with non-null data?
        if (isEmptyChartData(data)) {
            data = []
        }

        return render(view:'barChart', model: [columns: columns, name:'soilECForLocation', title:'Soil EC', data: data, colors: colors])
    }

    def soilpHForLocation() {

        def litmusColors = getLitmusColors()

        def columns = [['string',"Depth"]]
        litmusColors.each {
            columns << ['number', "pH ${it.pH}"]
        }

        def realData = studyLocationService.getSoilPhForStudyLocation(params.studyLocationName)

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
            litmusColors.each { color ->
                if (!found && color.pH > element.ph) {
                    found = true;
                    if (row.size() > 1) {
                        row.pop()
                    }
                    row << element.ph
                    row << 0
                } else {
                    row << 0
                }
            }
            adjustedData << row
        }

        def colors = litmusColors.collect { it.color }

        render(view:'barChart', model:[name:'soilPhForLocation', title:'Soil pH', columns: columns, data: adjustedData, colors: colors])
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

    private static getLitmusColors() {
        def litmusColors = [
            [pH:0, color:'#F61800'],
            [pH:1, color:'#F76502'],
            [pH:2, color:'#FCCC00'],
            [pH:3, color:'#FFFF02'],
            [pH:4, color:'#CCFF33'],
            [pH:5, color:'#56FF00'],
            [pH:6, color:'#5AB700'],
            [pH:7, color:'#1D6632'],
            [pH:8, color:'#2E9965'],
            [pH:9, color:'#36B7BE'],
            [pH:10, color:'#3398FF'],
            [pH:11, color:'#0066FF'],
            [pH:12, color:'#0000FF'],
            [pH:13, color:'#000099'],
            [pH:14, color:'#663266'],
        ]
        return litmusColors
    }


}
