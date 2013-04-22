package au.org.ala.soils2sat

class StudyLocationVisit implements Serializable {

    String studyLocationName
    String studyLocationVisitId

    static constraints = {
        studyLocationName nullable: false
        studyLocationVisitId nullable: false
    }
}
