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

    $("#image-container").html('<img src="${createLink(controller:'attachment', action:'downloadAsImage', id: attachment.id)}" image-width="${imageWidth}" image-height="${imageHeight}" alt="" />');

    var overallHeight = $("#modal_element_id").height();
    if (overallHeight) {
        var headerHeight = $(".modal-header").height();
        var containerHeight = (overallHeight - (headerHeight + 80));  // padding
        $("#image-container").css("height", containerHeight);
    }

</script>