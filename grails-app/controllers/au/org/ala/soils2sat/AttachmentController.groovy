package au.org.ala.soils2sat

import org.codehaus.groovy.grails.plugins.codecs.MD5Codec
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

class AttachmentController {

    def springSecurityService
    def attachmentService

    def upload() {

        def attachment = new Attachment()

        attachment.attachedTo = AttachmentOwnerType.StudyLocation

        [attachment: attachment]
    }

    def edit() {
        def attachment = Attachment.get(params.int("id"))
        [attachment: attachment]
    }

    def saveAttachment() {

        def attachment = Attachment.get(params.int("id"))

        def fileList = []

        if (!attachment) {
            attachment = new Attachment(params)

            def mprequest = request as MultipartHttpServletRequest
            for (String paramName : mprequest.getMultiFileMap().keySet()) {
                List<MultipartFile> files = mprequest.getMultiFileMap().get(paramName);
                files.each { file ->
                    if (file.size > 0) {
                        fileList << file
                    }
                }
            }

            if (!fileList) {
                flash.errorMessage = "You must choose a file to upload!"
                render(view:'upload', model: [attachment:attachment])
                return
            }
            attachment = new Attachment(params)
        } else {
            attachment.setProperties(params)
        }

        if (!attachment.attachedTo) {
            flash.errorMessage = "Select either StudyLocation or StudyLocationVisit"
            render(view:'upload', model: [attachment:attachment])
            return
        }

        if (!attachment.studyLocationName) {
            flash.errorMessage = "You must enter a valid study location name"
            render(view:'upload', model: [attachment:attachment])
            return
        }


        if (attachment.attachedTo == AttachmentOwnerType.StudyLocationVisit) {
            if (!attachment.studyLocationVisitStartDate) {
                flash.errorMessage = "You must specify a visit start date!"
                render(view:'upload', model: [attachment:attachment])
                return
            }
        } else {
            attachment.studyLocationVisitStartDate = null
        }

        if (!attachment.category) {
            flash.errorMessage = "No category specified"
            render(view:'upload', model: [attachment:attachment])
            return
        }

        if (fileList) {
            def mprequest = request as MultipartHttpServletRequest
            fileList.each { file ->
            }
            for (String paramName : mprequest.getMultiFileMap().keySet()) {
                List<MultipartFile> files = mprequest.getMultiFileMap().get(paramName);
                files.each { file ->
                    if (file != null) {
                        attachmentService.saveAttachment(attachment.properties, file)
                    }
                }
            }
        } else {
            if (attachment.id) {
                attachment.save(failOnError: true)
            }
        }

        redirect(controller: 'admin', action:'attachments')
    }

    def download() {
        def attachment = Attachment.get(params.int("id"))
        if (attachment) {
            def bytes = attachmentService.getAttachmentBytes(attachment)
            def filename = attachment.name ?: UUID.randomUUID().toString()
            response.contentType = attachment.mimeType
            response.setHeader("Content-disposition", "attachment;filename=${filename}")
            response.outputStream.write(bytes)
            response.flushBuffer()
        }
    }

    def downloadAsImage() {
        def attachment = Attachment.get(params.int("id"))
        if (attachment) {
            def image = attachmentService.getAttachmentAsImage(attachment)
            def filename = attachment.name ?: UUID.randomUUID().toString()
            // Preserve the mime type if attachment is actually an image, otherwise its image representation will be jpeg
            def mimeType = (attachment.mimeType?.startsWith('image/') ? attachment.mimeType : 'image/jpeg')
            response.setContentType(mimeType)
            response.setHeader("Content-disposition", "attachment;filename=${filename}")
            response.outputStream.write(ImageUtils.imageToBytes(image))
            response.flushBuffer()
        }
    }

    def delete() {
        def attachment = Attachment.get(params.int("id"))
        if (attachment) {
            attachmentService.deleteAttachment(attachment)
        }
        redirect(controller:'admin', action:'attachments')
    }

    def imagePreviewFragment() {
        def attachment = Attachment.get(params.int("id"))

        // calculate the images size
        def height = 0
        def width = 0
        if (attachment) {
            def bufferedImage = attachmentService.getAttachmentAsImage(attachment)
            height = bufferedImage.height
            width = bufferedImage.width
        }

        [attachment: attachment, imageHeight: height, imageWidth: width]
    }

    def thumbnailFragment() {
        def attachment = Attachment.get(params.int("id"))
        [attachment: attachment]
    }

    def downloadThumbnail() {
        def attachment = Attachment.get(params.int("id"))
        if (attachment) {
            def thumbnail = attachment?.thumbnail
            if (!thumbnail) {
                thumbnail = attachmentService.generateThumbnail(attachment)
            }

            if (thumbnail) {
                response.setContentType("image/jpeg")
                response.setHeader("Content-disposition", "attachment;filename=${attachment.attachmentId}_thumb.jpg")
                response.outputStream.write(thumbnail?.bytes)
                response.flushBuffer()
            }
        }
    }

    def imageViewerToolbarFragment() {
        def attachment = Attachment.get(params.int("id"))
        [attachment: attachment]
    }

}
