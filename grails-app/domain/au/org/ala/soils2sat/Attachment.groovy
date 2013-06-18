package au.org.ala.soils2sat

class Attachment {

    String ownerId // In the case of a study location, its the name, a visit will be studyLocationName_dateOfFirstVisit (yyyymmdd)
    AttachmentCategory category
    String name
    String mimeType
    long size
    byte[] bytes

    static constraints = {
    }
}
