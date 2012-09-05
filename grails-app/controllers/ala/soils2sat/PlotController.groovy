package ala.soils2sat

class PlotController {

    def detailsFragment() {

        def plotName = params.plotName;

        [plotName:plotName]
    }
}
