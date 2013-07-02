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

import org.codehaus.groovy.grails.web.json.JSONElement

public class StudyLocationDetails {

    JSONElement data

    public StudyLocationDetails(JSONElement data) {
        this.data = data
    }

    Integer getId() {
        return data.id as Integer
    }

    String getSiteLocationName() {
        return data.siteLocationName as String
    }

    String getEstablishedDate() {
        return data.establishedDate as String
    }

    String getDescription() {
        return data.description as String
    }

    String getBioregionName() {
        return data.bioregionName as String
    }

    String getProperty() {
        return data.property as String
    }

    String getZone() {
        return data.zone as String
    }

    String getEasting() {
        return data.easting as String
    }

    String getNorthing() {
        return data.northing as String
    }

    String getMethod() {
        return data.method as String
    }

    String getDatum() {
        return data.datum as String
    }

    Boolean getPlotPermanentlyMarkedq() {
        return data.plotPermanentlyMarkedq as Boolean
    }

    Boolean getPlotAlignedToGridq() {
        return data.plotAlignedToGridq as Boolean
    }

    String getLandformPattern() {
        return data.landformPattern as String
    }

    String getLandformElement() {
        return data.landformElement as String
    }

    String getSiteSlope() {
        return data.siteSlope as String
    }

    String getSiteAspect() {
        return data.siteAspect as String
    }

    String getSurfaceStrewSize() {
        return data.surfaceStrewSize as String
    }

    Boolean getPlot100mBy100m() {
        return data.plot100mBy100m as String
    }

    String getComments() {
        return data.comments as String
    }

}
