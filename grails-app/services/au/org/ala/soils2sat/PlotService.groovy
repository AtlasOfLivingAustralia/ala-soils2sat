package au.org.ala.soils2sat

import javax.servlet.http.HttpServletResponse
import ala.soils2sat.CodeTimer
import grails.converters.JSON

class PlotService {

    def grailsApplication

    private Map<String, String> _serviceCache = [:]

    List<PlotSearchResult> getPlots() {
        def plots = proxyServiceCall("getStudyLocations")
        def results = new ArrayList<PlotSearchResult>()
        plots?.results?.each { plot ->
            def result = new PlotSearchResult(siteName: plot.siteName, date: plot.date, longitude: plot.longitude, latitude: plot.latitude)
            results.add(result)
        }

        return results
    }

    List<PlotSearchResult> searchPlots(String query, BoundingBox boundingBox) {
        def results = new ArrayList<PlotSearchResult>()
        def plots = proxyServiceCall("getStudyLocations")?.results
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
        def plots = proxyServiceCall("getStudyLocationSummary", [siteNames: plotName])?.studyLocationSummaryList
        PlotSearchResult result = null
        if (plots) {
            plots.each { plot ->
                result = new PlotSearchResult(siteName: plot.siteLocationName, date: plot.lastVisitDate, longitude: plot.longitude, latitude: plot.latitude)
            }
        }
        return result
    }

    private def proxyServiceCall(String servicePath, Map params = null) {

        String url = "${grailsApplication.config.aekosServiceRoot}/${servicePath}"
        if (params) {
            url += '?'
            params.each {
                url += it.key + "=" + it.value + '&'
            }
            if (url.endsWith('&')) {
                url = url.subSequence(0, url.length() - 1);
            }
        }

        if (_serviceCache.containsKey(url)) {
            return JSON.parse(_serviceCache[url])
        }

        def timer = new CodeTimer("Service call: ${url}")

        try {
            def u = new URL(url)
            def results = u.getText()
            _serviceCache.put(url, results)
            return JSON.parse(results)
        } finally {
            timer.stop(true)
        }
    }

}
