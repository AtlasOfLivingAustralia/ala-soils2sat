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

class StudyLocationVisitSearchResult {

    String studyLocationId
    String studyLocationName
    String studyLocationVisitId
    String visitStartDate
    String visitEndDate
    List<VisitObserverTO> observers
    PointTO point
}

class PointTO {
    double easting
    double northing
    double latitude
    double longitude
    String zone
    String studyLocationName
    String wkt
}
