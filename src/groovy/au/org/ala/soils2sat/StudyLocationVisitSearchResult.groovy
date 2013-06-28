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
