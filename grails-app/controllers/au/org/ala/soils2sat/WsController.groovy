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

class WsController {

    def studyLocationService

    def index() {
        redirect(action:'rifcs')
    }

    def rifcs() {
        def extracts = DataExtraction.findAllByDoiIsNotNull()
        response.contentType = "text/xml"
        def locationMap = [:]
        extracts.each { extract ->
            def locations = [:]
            extract.studyLocationVisits?.each {
                def bits = it.split("_")
                if (bits && bits.length > 0) {
                    def studyLocationName = bits[0]
                    def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
                    locations[studyLocationName] = [latitude:studyLocationDetails.latitude, longitude: studyLocationDetails.longitude]
                }
            }
            locationMap[extract] = locations
        }

        render(template: 'RIFCS', model:[extracts: extracts, locationMap: locationMap])
    }

}
