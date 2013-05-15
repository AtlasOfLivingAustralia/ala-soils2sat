package au.org.ala.soils2sat

import grails.converters.JSON
import ala.soils2sat.CodeTimer
import grails.plugin.cache.CacheEvict
import grails.plugin.cache.Cacheable

class LayerService {

    def grailsApplication
    def logService

    @Cacheable("S2S_LayerCache")
    def getLayerInfo(String layerName) {
        def results = "{}"
        def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/layer/${layerName}")
        results = url.getText()
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

    @Cacheable("S2S_LayerCache")
    List<String> getValuesForField(String fieldName) {
        def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/objects/${fieldName}")
        def data = JSON.parse(url.getText())
        def results = []
        data.each {
            results << it.name
        }
        return results
    }

    @Cacheable("S2S_LayerCache")
    Map<String, String> getIntersectValues(Double lat, Double lon, List<String> layerNames) {
        def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layerNames.join(",")}/${lat}/${lon}")
        def data = JSON.parse(url.getText())
        def results = [:]
        data.each {
            results[it.field] = it.value
        }
        return results
    }

    @CacheEvict("S2S_LayerCache")
    public void flushCache() {
        logService.log("Flushing Layer Cache")
    }

}
