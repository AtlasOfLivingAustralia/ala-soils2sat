package au.org.ala.soils2sat

class Attachment {

    String attachmentId
    Date dateUploaded
    User uploadedBy
    String ownerId // In the case of a study location, its the name, a visit will be studyLocationName_dateOfFirstVisit (yyyymmdd)
    AttachmentCategory category
    String name
    String mimeType
    long size
    String comment

    static hasOne = [thumbnail:Thumbnail]

    static constraints = {
        attachmentId nullable: true
        uploadedBy nullable: false
        dateUploaded nullable: false
        ownerId nullable: false
        category nullable: false
        name nullable: true
        mimeType nullable: true
        comment nullable: true
        thumbnail nullable: true
    }

}
