<div>
    <div id="image-container" >
        <sts:spinner />
    </div>
</div>

<style type="text/css">

    #image-container {
        width: 100%;
        height: 400px;
    }

    #image-container img {
        max-width: inherit !important;
    }

</style>


%{--<div style="overflow: scroll">--}%
    %{--<img src="${createLink(controller:'attachment', action:'download', id: attachment.id)}" />--}%
%{--</div>--}%

<script type="text/javascript">

    $.ajax("${createLink(controller:'attachment', action:'imageViewerToolbarFragment', id: attachment.id)}").done(function(html) {
        $(".modal-header").html(html);
        $("#image-container img").panZoom({
            debug: false,
            pan_step: 20,
            zoom_step: 5,
            min_width: 200,
            min_height: 200,
            mousewheel: true,
            mousewheel_delta: 2,
            draggable: true,
            'zoomIn': $('#btnZoomIn'),
            'zoomOut': $('#btnZoomOut'),
            'panUp': $('#btnPanUp'),
            'panDown': $('#btnPanDown'),
            'panLeft': $('#btnPanLeft'),
            'panRight': $('#btnPanRight')
        });
    });

    $("#image-container").html('<img src="${createLink(controller:'attachment', action:'download', id: attachment.id)}" image-width="${imageWidth}" image-height="${imageHeight}" alt="">');

</script>