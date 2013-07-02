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
