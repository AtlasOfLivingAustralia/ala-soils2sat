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

class DOIService {

    static transactional = false

    def grailsApplication
    def grailsLinkGenerator
    def settingService

    def mintDOI(DataExtraction extraction, String creator) {
        if (!extraction) {
            throw new RuntimeException("Extraction package is null!")
        }

        def landingPageUrl = grailsLinkGenerator.link(controller: 'extract', action:'landingPage', params:[packageName: extraction.packageName], absolute: false)
        def cfg = grailsApplication.config
        try {
            def post = new URL(settingService.DOIServiceUrl).openConnection();
            def message = """{
              "creator":"${creator}",
              "title":"Data extract ${extraction.packageName}",
              "landingPageUrl":"${cfg.doiS2SRoot}${landingPageUrl}"
            }"""
            println "POSTing DOI minting request ${message}"
            post.setRequestMethod("POST")
            post.setDoOutput(true)
            post.setRequestProperty("Content-Type", "application/json")
            post.getOutputStream().write(message.getBytes("UTF-8"))
            post.getResponseCode() // will throw for non-OK response codes
            def doi = post.getInputStream().getText()
            println "Successfully minted DOI=${doi}"
            return doi
        } catch (Exception ex) {
            throw new DOIMintingFailedException(ex.message)
        }
    }
}
