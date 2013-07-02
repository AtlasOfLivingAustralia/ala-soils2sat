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

import javax.persistence.Transient

class UserApplicationState implements Serializable {

    // List<StudyLocation> selectedPlots
    List<StudyLocationVisit> selectedVisits
    List<EnvironmentalLayer> layers
    Boolean plotOnlySelectedLocations
    Bounds viewExtent
    Date lastLogin
    UserSearch currentSearch
    String sidebarSelectedTab

    static hasMany = [layers: EnvironmentalLayer, selectedVisits:StudyLocationVisit]

    static belongsTo = [user:User]

    static constraints = {
        // selectedPlots nullable: true
        selectedVisits nullable: true
        layers nullable: true
        plotOnlySelectedLocations nullable:true
        viewExtent nullable: true
        lastLogin nullable: true
        currentSearch nullable: true
        sidebarSelectedTab nullable: true
    }

    @Transient
    public boolean containsLayerName(String layerName) {
        def existing = layers?.find {
            it.name == layerName
        }
        return existing != null
    }

    @Transient
    public List<String> getSelectedPlotNames() {
        def names = []
        selectedVisits.collect {
            if (!names.contains(it.studyLocationName)) {
                names << it.studyLocationName
            }
        }
        return names
    }

    @Transient
    public boolean containsPlot(String name) {
        def existing = getSelectedPlotNames()?.find {
            it == name
        }

        return existing != null
    }

    @Transient
    public boolean containsVisit(String visitId) {
        def existing = selectedVisits?.find {
            it.studyLocationVisitId == visitId
        }
    }

}
