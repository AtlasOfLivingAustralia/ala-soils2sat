%{--
- ﻿Copyright (C) 2013 Atlas of Living Australia
- All Rights Reserved.
-
- The contents of this file are subject to the Mozilla Public
- License Version 1.1 (the "License"); you may not use this file
- except in compliance with the License. You may obtain a copy of
- the License at http://www.mozilla.org/MPL/
-
- Software distributed under the License is distributed on an "AS
- IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
- implied. See the License for the specific language governing
- rights and limitations under the License.
--}%

<ul class="thumbnails">
    <g:each in="${attachments}" var="attachment">
        <li class="span2" attachmentId="${attachment.id}">
            <div style="text-align: center">
                <a href="#" class="thumbnail" style="height: 140px" title="${attachment.name} (${attachment.mimeType})">
                    <sts:spinner />
                </a>
                <div style="float: right; background-color: transparent; margin-top: -20px; padding-right: 5px;">
                    <g:if test="${attachment.comment}">
                        <i class="icon-comment infoButton" title="${attachment.comment}"></i>
                    </g:if>
                    <span style="clear: both" />
                </div>
            </div>
        </li>
    </g:each>
</ul>

<script type="text/javascript">

    $("a.thumbnail").each(function(index, element) {
        var attachmentId = $(this).parents("[attachmentId]").attr("attachmentId");
        $.ajax("${createLink(controller:'attachment', action:'thumbnailFragment')}/" + attachmentId).done(function(html) {
            $("li[attachmentId=" + attachmentId + "] a.thumbnail").html(html);
        });
    });

    $("a.thumbnail").click(function(e) {
        e.preventDefault();
        var attachmentId = $(this).parents("[attachmentId]").attr("attachmentId");
        if (attachmentId) {
            showModal({
                url: "${createLink(controller: 'attachment', action:'imagePreviewFragment')}/" + attachmentId,
                title: "Image Preview",
                height: 500,
                width: 800
            });
        }
    });

</script>
