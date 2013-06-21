package au.org.ala.soils2sat

class AttachmentStorageService {

    public String storeAttachmentFile(Attachment attachment, byte[] filedata) {
        def attachmentFile = new AttachmentFile(bytes: filedata)
        attachmentFile.save(failOnError: true)
        return attachmentFile.attachmentId
    }

    public void deleteAttachmentFile(String attachmentId) {
        AttachmentFile.executeUpdate("delete AttachmentFile af where af.attachmentId = :attachmentId", [attachmentId: attachmentId])
    }

    public byte[] retrieveAttachmentFile(String attachmentId) {
        def attachmentFile = AttachmentFile.findByAttachmentId(attachmentId)
        if (attachmentFile) {
            return attachmentFile.bytes
        }
        return null
    }

}
