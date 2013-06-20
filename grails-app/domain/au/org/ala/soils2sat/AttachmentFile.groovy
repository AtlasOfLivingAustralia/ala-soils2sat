package au.org.ala.soils2sat

class AttachmentFile {

    String attachmentId
    byte[] bytes

    def beforeValidate() {
        if (!attachmentId) {
            attachmentId = UUID.randomUUID()
        }
    }

    static constraints = {
        attachmentId nullable: false
        bytes nullable: false
    }
}
