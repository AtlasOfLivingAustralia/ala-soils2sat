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

class SpatialProxyController {

    def studyLocationService

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
        def studyLocationName = params.studyLocationName as String

        def studyLocation = studyLocationService.getStudyLocationDetails(studyLocationName)
        if (layers && studyLocation) {
            def url = new URL("${grailsApplication.config.spatialPortalRoot}/ws/intersect/${layers}/${studyLocation.latitude}/${studyLocation.longitude}")
            response.setContentType("application/json")
            render url.getText()
            return
        }

        render([] as JSON)
    }
}
