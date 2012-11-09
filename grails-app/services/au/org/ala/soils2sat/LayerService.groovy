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

    List<LayerDefinition> getAllLayers() {
        def results = new ArrayList<LayerDefinition>()
        def timer = new CodeTimer("getAllLayers")
        try {
            def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/layers")
            def layerJson = url.getText()
            def layerData = JSON.parse(layerJson)

            layerData.each {
                def layer = new LayerDefinition()
                it.each {
                    if (it.value && layer.hasProperty(it.key)) {
                        layer[it.key] = it.value
                    }
                }
                results.add(layer)
            }
            return results
        } finally {
            timer.stop(true)
        }
    }

}
