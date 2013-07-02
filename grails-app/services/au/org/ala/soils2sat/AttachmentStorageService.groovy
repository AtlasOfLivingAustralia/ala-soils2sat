/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

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
