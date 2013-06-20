<ul class="thumbnails">
    <g:each in="${attachments}" var="attachment">
        <li class="span2" attachmentId="${attachment.id}">
            <div style="text-align: center">
                <a href="#" class="thumbnail" style="height: 140px">
                    <sts:spinner />
                </a>
                <div style="float: right; background-color: transparent; margin-top: -20px; padding-right: 5px;">
                    <i style="background-color: #808080; border-radius: 2px" class="icon-info-sign icon-white infoButton" title="${attachment.name}"></i>
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
