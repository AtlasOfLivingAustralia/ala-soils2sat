package au.org.ala.soils2sat

class StudyLocationTO {
    String studyLocationName
    String studyLocationId
    Double latitude
    Double longitude
    String firstVisit
    String lastVisit
    String easting
    String northing
    String mgaZone
    List<String> observers

//    public StudyLocationTO(Map entries) {
//        this.properties.putAll(entries)
//    }

    Boolean selected
}
