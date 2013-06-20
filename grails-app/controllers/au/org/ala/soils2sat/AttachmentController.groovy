package au.org.ala.soils2sat

import org.codehaus.groovy.grails.plugins.codecs.MD5Codec
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

class AttachmentController {

    def springSecurityService
    def attachmentService

    def upload() {

    }

    def saveAttachment() {

        if (!params.file) {
            flash.errorMessage = "You must choose a file to upload!"
            redirect(controller:'attachment', action:'upload', params: params)
            return
        }

        if (!params.attachTo) {
            flash.errorMessage = "Select either StudyLocation or StudyLocationVisit"
            redirect(controller:'attachment', action:'upload', params: params)
            return
        }

        if (!params.studyLocationName) {
            flash.errorMessage = "You must enter a valid study location name"
            redirect(controller:'attachment', action:'upload', params: params)
            return
        }

        if (params.attachTo == 'studyLocationVisit' && !params.studyLocationVisitStartDate) {
            flash.errorMessage = "You must specify a visit start date!"
            redirect(controller:'attachment', action:'upload', params: params)
            return
        }

        def category = params.category as AttachmentCategory

        if (!category) {
            flash.errorMessage = "No category specified"
            redirect(controller:'attachment', action:'upload', params: params)
            return
        }

        def ownerId = "${params.studyLocationName}"
        if (params.attachTo == 'studyLocationVisit') {
            ownerId += "_${params.studyLocationVisitStartDate}"
        }

        if(request instanceof MultipartHttpServletRequest) {
            def mprequest = request as MultipartHttpServletRequest
            for (String paramName : mprequest.getMultiFileMap().keySet()) {
                List<MultipartFile> files = mprequest.getMultiFileMap().get(paramName);
                files.each { file ->
                    if (file != null) {
                        def attachment = attachmentService.saveAttachment(ownerId, category, params.comment, file)
                        if (attachment) {
                            attachmentService.generateThumbnail(attachment)
                        }
                    }
                }
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

    def delete() {
        def attachment = Attachment.get(params.int("id"))
        if (attachment) {
            attachmentService.deleteAttachment(attachment)
        }
        redirect(controller:'admin', action:'attachments')
    }

    def imagePreviewFragment() {
        def attachment = Attachment.get(params.int("id"))
        [attachment: attachment]
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

}
