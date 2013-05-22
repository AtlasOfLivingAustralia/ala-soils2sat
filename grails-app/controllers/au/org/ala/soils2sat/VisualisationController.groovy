package au.org.ala.soils2sat

class VisualisationController {

    def studyLocationService
    def biocacheService

    def studyLocationVisualisations() {

        [studyLocationName: params.studyLocationName]
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

        [columns: columns, data: data]
    }

    def soilECForLocation() {

        def columns = [
            ['string',"Depth"],
            ['number', "Soil EC"]
        ]

        def samplingUnitData = studyLocationService.getSoilECForStudyLocation(params.studyLocationName)
        def data = samplingUnitData?.collect { ["${it.upperDepth} - ${it.lowerDepth}", it.EC ]}


        // is there at least one row with non-null data?
        def nonNull = data.find { it[1] != null }
        if (!nonNull) {
            data = []
        }

        def colors = [ '#4E81BD' ]

        return [columns: columns, data: data, colors: colors]
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

        [columns: columns, data: adjustedData, colors: colors]
    }

    public weedNonWeedBreakdownForLocation() {

        def weedList = biocacheService.getWeedsOfNationalSignificance()?.sort { it }

        def ausplotsNames = studyLocationService.getVoucheredTaxaForStudyLocation(params.studyLocationName)

        println weedList
        println ausplotsNames

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

        [columns: columns, data: data, colors: colors]
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
