package au.org.ala.soils2sat

class SpatialProxyController {

    def layersSearch = {
        proxyService('/ws/layers/search')
    }

    def proxyService(String query) {
        def paramsClone = params.clone();
        paramsClone.remove('action')
        paramsClone.remove('controller')

        def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/layers/search" + paramsClone.toQueryString())

        response.setContentType("application/json")
        render url.getText()
    }
}
