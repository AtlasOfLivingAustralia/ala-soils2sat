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

class StudyLocationDetailsTO implements Serializable {
    String bioregionName
    String easting
    String firstVisitDate
    String landformElement
    String landformPattern
    String lastVisitDate
    Double latitude
    Double longitude
    String mgaZone
    String northing
    Integer numberOfDistinctPlantSpecies
    Integer numberOfVisits
    List<String> observers
    List<SamplingUnitTO> samplingUnits
    String studyLocationId
    String studyLocationName
}

class SamplingUnitTO implements Serializable {
    String description
    String id
}
