package au.org.ala.soils2sat

import grails.converters.JSON
import ala.soils2sat.CodeTimer
import grails.plugin.springcache.annotations.CacheFlush
import org.springframework.cache.annotation.Cacheable

class LayerService {

    def grailsApplication
    def logService

    @Cacheable("S2S_LayerCache")
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

    @Cacheable("S2S_LayerCache")
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

    @CacheFlush("S2S_LayerCache")
    public void flushCache() {
        logService.log("Flushing Layer Cache")
    }

}
