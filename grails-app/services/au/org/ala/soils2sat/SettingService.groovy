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

class SettingService {

    public static String OBSERVATION_RADIUS_KEY = "ala.observation.radius"
    public static String OBSERVATION_RANK_KEY = "ala.observation.rank"
    public static String OBSERVATION_FILTER_KEY = "ala.observation.taxonFilter"
    public static String LAYER_STYLES_KEY = "ala.layer.styles"
    public static String DOI_SERVICE_URL_KEY = "tern.doi.service.url.base"
    public static String DOI_SERVICE_USERNAME_KEY = "tern.doi.service.username"
    public static String DOI_SERVICE_APP_ID_KEY = "tern.doi.service.app.id"
    public static String RIFCS_DEFAULT_CITATION_NAME_KEY = "rifcs.default.citation.name"

    public String getDOIServiceUrl() {
        def setting = Setting.findByKey(DOI_SERVICE_URL_KEY)
        if (!setting) {
            setting = new Setting(key: DOI_SERVICE_URL_KEY, value: "https://doi.tern.uq.edu.au/test/", comment: "The service url for the TERN DOI Minting service")
            setting.save()
        }
        return setting.value
    }

    public String getDOIServiceUsername() {
        def setting = Setting.findByKey(DOI_SERVICE_USERNAME_KEY)
        if (!setting) {
            setting = new Setting(key: DOI_SERVICE_USERNAME_KEY, value: "David.Baird@csiro.au", comment: "Name of a user registered to use the TERN DOI Minting service.")
            setting.save()
        }
        return setting.value
    }

    public String getDOIServiceAppId() {
        def setting = Setting.findByKey(DOI_SERVICE_APP_ID_KEY)
        if (!setting) {
            setting = new Setting(key: DOI_SERVICE_APP_ID_KEY, value: "4367305c3431f6ac3166953b56ea27e5", comment: "The APP ID to use when calling TERN DOI Minting service - must be affiliated with the DOI Minting Service Username setting.")
            setting.save()
        }
        return setting.value
    }


    public float getObservationRadius() {
        def setting = Setting.findByKey(OBSERVATION_RADIUS_KEY)
        if (!setting) {
            setting = new Setting(key: OBSERVATION_RADIUS_KEY, value: "0.5", comment:"The radius of the circle around a location in include when looking for observation records in the ALA (Kms)")
            setting.save()
        }
        return Float.parseFloat(setting.value)
    }

    public String getObservationsRank() {
        def setting = Setting.findByKey(OBSERVATION_RANK_KEY)
        if (!setting) {
            setting = new Setting(key: OBSERVATION_RANK_KEY, value: "species", comment:"The rank of the taxanomic names to return when looking for observation records close to a locality (e.g. species or genus)")
            setting.save()
        }
        return setting.value
    }

    public String getTaxonFilter() {
        def setting = Setting.findByKey(OBSERVATION_FILTER_KEY)
        if (!setting) {
            setting = new Setting(key: OBSERVATION_FILTER_KEY, value: "kingdom:Plantae", comment:"The filter expression to apply to ALA queries (Default 'kingdom:Plantae'")
            setting.save()
        }
        return setting.value
    }

    public String getLayerStyles() {
        def setting = Setting.findByKey(LAYER_STYLES_KEY)
        if (!setting) {
            def defaultValues = ['ibra7_regions':'ibra7_labels_style', 'ibra7_subregions':'ibra7_subregion_labels_style']
            setting = new Setting(key: LAYER_STYLES_KEY, value: "[]", comment:"The filter expression to apply to ALA queries (Default 'kingdom:Plantae'")
            setting.save()
        }
        return setting.value
    }

    public String getRifcsDefaultCitationName() {
        def setting = Setting.findByKey(RIFCS_DEFAULT_CITATION_NAME_KEY)
        if (!setting) {
            setting = new Setting(key: RIFCS_DEFAULT_CITATION_NAME_KEY, value: "Pullan, Martin", comment:"The default name to use for citations if none is given during the extract")
            setting.save()
        }
        return setting.value
    }


}
