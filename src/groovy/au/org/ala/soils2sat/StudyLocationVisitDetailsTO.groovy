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
