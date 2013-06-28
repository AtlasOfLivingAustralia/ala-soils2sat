package au.org.ala.soils2sat

class Attachment {

    String attachmentId
    Date dateUploaded
    User uploadedBy
    String originalName
    AttachmentOwnerType attachedTo
    String studyLocationName
    String studyLocationVisitStartDate
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
        attachedTo nullable: false
        studyLocationName nullable: false
        studyLocationVisitStartDate nullable: true
        category nullable: false
        originalName nullable: false
        name nullable: true
        mimeType nullable: true
        comment nullable: true
        thumbnail nullable: true
    }

}
