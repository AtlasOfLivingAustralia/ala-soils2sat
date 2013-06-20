package au.org.ala.soils2sat

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.web.multipart.MultipartFile

class AttachmentService {

    def springSecurityService
    def attachmentStorageService

    private static final int THUMBNAIL_SIZE = 100

    public Attachment saveAttachment(String ownerId, AttachmentCategory category, String comment, MultipartFile file) {
        def attachment = new Attachment(uploadedBy: springSecurityService.currentUser as User, dateUploaded: new Date(), ownerId: ownerId, category: category, name: file.originalFilename, mimeType: file.contentType, size: file.size, comment: comment)
        attachment.attachmentId =  attachmentStorageService.storeAttachmentFile(attachment, file.bytes)
        attachment.save(failOnError: true)
        return attachment
    }

    public byte[] getAttachmentBytes(Attachment attachment) {
        if (attachment) {
            return attachmentStorageService.retrieveAttachmentFile(attachment.attachmentId)
        }
        return null
    }

    public Thumbnail generateThumbnail(Attachment attachment) {
        if (attachment.mimeType?.startsWith("image/")) {
            def bytes = attachmentStorageService.retrieveAttachmentFile(attachment.attachmentId)
            if (bytes) {
                def bufferedImage = ImageUtils.bytesToImage(bytes)
                if (bufferedImage) {
                    bufferedImage = ImageUtils.scaleWidth(bufferedImage, THUMBNAIL_SIZE)
                    def thumbnail = new Thumbnail(bytes: ImageUtils.imageToBytes(bufferedImage), attachment: attachment)
                    thumbnail.save()
                    return thumbnail
                }
            }
        }
        return null; // Thumbnail could not be created...
    }

    public void deleteAttachment(Attachment attachment) {
        if (attachment) {
            // First delete any thumbnail
            attachment.thumbnail?.delete()
            // delete the stored bytes
            attachmentStorageService.deleteAttachmentFile(attachment.attachmentId)
            // and finally, delete the meta data
            attachment.delete()
        }
    }

}
