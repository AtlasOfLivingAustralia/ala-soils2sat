package au.org.ala.soils2sat

class PlotService extends ServiceBase {

    def grailsApplication

    List<PlotSearchResult> getPlots() {
        def plots = proxyServiceCall(grailsApplication, "getStudyLocations")
        def results = new ArrayList<PlotSearchResult>()
        plots?.results?.each { plot ->
            def result = new PlotSearchResult(siteName: plot.siteName, date: plot.date, longitude: plot.longitude, latitude: plot.latitude)
            results.add(result)
        }

        return results
    }

    List<PlotSearchResult> searchPlots(String query, BoundingBox boundingBox) {
        def results = new ArrayList<PlotSearchResult>()
        def plots = proxyServiceCall(grailsApplication, "getStudyLocations")?.results
        def q = query?.toLowerCase()

        if (query == "*") {
            query = ''; // force match all...
        }
        if (plots) {
            plots.each { plot ->
                boolean match = true
                if (query) {
                    if (!plot.siteName?.toLowerCase()?.contains(q)) {
                        match = false
                    }
                }

                if (boundingBox && match) {
                    if (!boundingBox.contains(plot.longitude, plot.latitude)) {
                        match = false
                    }
                }

                if (match) {
                    def result = new PlotSearchResult(siteName: plot.siteName, date: plot.date, longitude: plot.longitude, latitude: plot.latitude)
                    results.add(result)
                }
            }
        }

        return results
    }

    PlotSearchResult getPlotSummary(String plotName) {
        def plots = proxyServiceCall(grailsApplication, "getStudyLocationSummary", [siteNames: plotName])?.studyLocationSummaryList
        PlotSearchResult result = null
        if (plots) {
            plots.each { plot ->
                result = new PlotSearchResult(siteName: plot.siteLocationName, date: plot.lastVisitDate, longitude: plot.longitude, latitude: plot.latitude)
            }
        }
        return result
    }

    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.aekosServiceRoot}"
    }
}
