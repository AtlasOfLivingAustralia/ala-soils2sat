package au.org.ala.soils2sat

class StudyLocationVisit {

    String studyLocationName
    String studyLocationVisitId

    static constraints = {
        studyLocationName nullable: false
        studyLocationVisitId nullable: false
    }
}
