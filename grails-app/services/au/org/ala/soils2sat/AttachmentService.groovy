package au.org.ala.soils2sat

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.web.multipart.MultipartFile

import java.awt.image.BufferedImage

class AttachmentService {

    def springSecurityService
    def attachmentStorageService

    private static final int THUMBNAIL_SIZE = 100

    public Attachment saveAttachment(Map properties, MultipartFile file) {

        def attachment = new Attachment(properties)
        attachment.name = file.originalFilename
        attachment.originalName = file.originalFilename
        attachment.dateUploaded = new Date()
        attachment.uploadedBy = springSecurityService.currentUser as User
        attachment.mimeType = file.contentType
        attachment.size = file.size
        attachment.attachmentId =  attachmentStorageService.storeAttachmentFile(attachment, file.bytes)

        attachment.save(failOnError: true)

        generateThumbnail(attachment)

        return attachment
    }

    public byte[] getAttachmentBytes(Attachment attachment) {
        if (attachment) {
            return attachmentStorageService.retrieveAttachmentFile(attachment.attachmentId)
        }
        return null
    }

    public BufferedImage getAttachmentAsImage(Attachment attachment) {

        if (attachment.mimeType?.startsWith("image/")) {
            // just return the raw bytes, as it is already an image
            return ImageUtils.bytesToImage(getAttachmentBytes(attachment))
        } else if (attachment.mimeType?.equalsIgnoreCase("application/pdf")) {
            return ImageUtils.createImageFromPDFBytes(getAttachmentBytes(attachment))
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
            // delete the stored bytes
            attachmentStorageService.deleteAttachmentFile(attachment.attachmentId)
            // First delete any thumbnail
            attachment.thumbnail?.delete()
            attachment.thumbnail = null

            // and finally, delete the meta data
            attachment.delete()
        }
    }

}
