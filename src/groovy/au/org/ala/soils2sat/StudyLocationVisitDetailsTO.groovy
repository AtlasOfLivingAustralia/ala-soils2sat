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

class StudyLocationVisitDetailsTO {
    String climaticCondition
    String disturbance
    String drainageType
    String erosionAbundance
    String erosionState
    String erosionType
    String locationDescription
    String microrelief
    List<VisitObserverTO> observers
    String pitMarkerDatum
    String pitMarkerEasting
    String pitMarkerLocationMethod
    String pitMarkerMgaZones
    String pitMarkerNorthing
    List<SamplingUnitTO> samplingUnits
    String soilObservationType
    String studyLocationId
    String studyLocationName
    String studyLocationVisitId
    String surfaceCoarseFragsAbundance
    String surfaceCoarseFragsLithology
    String surfaceCoarseFragsSize
    String surfaceCoarseFragsType
    String vegetationCondition
    String visitEndDate
    String visitNotes
    String visitStartDate
}

class VisitObserverTO {
    String id
    String observerName
    String siteLocationVisit
}
