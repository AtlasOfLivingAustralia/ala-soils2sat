package au.org.ala.soils2sat

class VisualisationController {

    def studyLocationVisualisations() {

        [studyLocationName: params.studyLocationName]
    }

    def plantSpeciesBreakdownByLocation() {

        def columns = [['string',"Label"],['number', "%"]]
        def data = [
            ["Both AusPlots & ALA", 100],
            ["AusPlots only", 100],
            ["ALA Only", 100]
        ]

        [columns: columns, data: data]
    }

    def soilECForLocation() {

        def columns = [
            ['string',"Depth"],
            ['number', "Soil EC"]
        ]

        def data = [
            ["0.00 - 0.05", 0.09 ],
            ["0.05 - 0.15", 0.1 ],
            ["0.15 - 0.30", 0.3 ]
        ];

        def colors = [ '#4E81BD' ]

        return [columns: columns, data: data, colors: colors]
    }

    def soilpHForLocation() {

        def litmusColors = getLitmusColors()

        def columns = [['string',"Depth"]]
        litmusColors.each {
            columns << ['number', "pH ${it.pH}"]
        }

        def data = [
            [depth: "0.00 - 0.05", ph: 9.7],
            [depth: "0.05 - 0.15", ph: 7 ],
            [depth: "0.15 - 0.30", ph: 5.3]
        ]

        def adjustedData = []

        data.each { element ->
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

        def columns = [
            ['string', 'Label'], ['number', 'abundance']
        ]

        def data = [
            ['Non-Weed species', 123],
            ['Weed species', 23]
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
