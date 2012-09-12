package au.org.ala.soils2sat

import grails.converters.JSON

class SpatialProxyController {

    def plotService

    def layersSearch = {
        proxyService('/ws/layers/search')
    }

    def proxyService(String query) {
        def paramsClone = params.clone();
        paramsClone.remove('action')
        paramsClone.remove('controller')

        def url = new URL("${grailsApplication.config.spatialPortalRoot}${query}" + paramsClone.toQueryString())

        response.setContentType("application/json")
        render url.getText()
    }

    def intersect = {

        def layers = params.layers
        def plotName = params.plotName

        def plot = plotService.getPlotSummary(plotName)
        if (layers && plot) {
            def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layers}/${latitude}/${longitude}")
            response.setContentType("application/json")
            render url.getText()
            return
        }

        render([] as JSON)
    }
}
