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