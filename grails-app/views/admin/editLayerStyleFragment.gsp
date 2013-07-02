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

<div class="form-horizontal">
    <div class="control-group">
        <label class="control-label" for='layerName'>Layer Name:</label>
        <div class="controls">

<g:textField class="input-large" name="layerName" id="layerName" placeholder="Layer name" value="${layerStyle?.layerName}" />
        </div>
    </div>

    <div class="control-group">
        <label class="control-label" for='layerName'>Style:</label>
        <div class="controls">
            <g:textField class="input-large" name="style" id="style" placeholder="Style" value="${layerStyle?.style}" />
        </div>
    </div>

    <div class="control-group">
        <div class="controls">
            <button class="btn btn-small" id="btnCancel">Cancel</button>
            <button class="btn btn-small btn-primary" id="btnSave">Save</button>
        </div>
    </div>

</div>

<script type="text/javascript">

    $("#btnCancel").click(function(e) {
        e.preventDefault();
        hideModal();
    });

    $("#btnSave").click(function(e) {
        e.preventDefault();
        var layerName = $("#layerName").val();
        var style = $("#style").val();
        if (layerName && style) {
            $.ajax("${createLink(action:'saveLayerStyleAjax', params:[layerStyleId: layerStyle?.id])}&layerName=" + layerName + "&style=" + style).done(function() {
                hideModal();
                window.location = "${createLink(action:'layerStyles')}";
            });
        }

    });

</script>