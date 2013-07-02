/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

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
