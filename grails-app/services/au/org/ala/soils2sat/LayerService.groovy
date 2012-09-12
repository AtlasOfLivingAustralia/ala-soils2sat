package au.org.ala.soils2sat

import grails.converters.JSON
import ala.soils2sat.CodeTimer

class LayerService {

    def grailsApplication

    def getLayerInfo(String layerName) {
        def results = "{}"
        def timer = new CodeTimer("LayerInfo")
        try {
            def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/layer/${layerName}")
            results = url.getText()
        } finally {
            timer.stop(true)
        }
        return JSON.parse(results)
    }

}
