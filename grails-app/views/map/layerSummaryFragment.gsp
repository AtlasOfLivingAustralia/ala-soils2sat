<table style="width:100%">
    <tr>
        <td><h5 style="margin-top: 0px; margin-bottom: 10px;">Selected Layer - ${layerDefinition.displayname}</h5></td>
        <td style="vertical-align: middle;">
            <button id="btnLayerSummaryLoadLayer" class="btn btn-small btn-primary pull-right"><i
                class="icon-plus icon-white"></i>&nbsp;Add Layer</button>
            <g:if test="${params.showInfoButton}">
                <button style="margin-right: 10px" id="btnLayerSummaryShowInfo" class="btn btn-small pull-right"><i
                    class="icon-info-sign"></i>&nbsp;Layer details</button>
            </g:if>
        </td>
    </tr>
</table>

<div>
    <table class="table table-condensed table-striped">
        <tr>
            <td>Description</td>
            <td>${layerDefinition.description}</td>
        </tr>
        <tr>
            <td>Classification</td>
            <td>
                <g:if test="${layerDefinition.classification1}">
                    <span class="label">${layerDefinition.classification1}</span>
                </g:if>
                <g:if test="${layerDefinition.classification2}">
                    <span class="label">${layerDefinition.classification2}</span>
                </g:if>
            </td>
        </tr>
        <tr>
            <td>Type/Keywords</td>
            <td><span class="label label-success">${layerDefinition.type}</span>
                <g:if test="${layerDefinition.keywords}">
                    <g:each in="${layerDefinition.keywords.split(',')}" var="keyword">
                        <span class="label label-info">${keyword}</span>
                    </g:each>
                </g:if>
            </td>
        </tr>
    </table>
</div>

<script type="text/javascript">

    $('#btnLayerSummaryLoadLayer').click(function (e) {
        e.preventDefault();
        ${params.callback ? "${params.callback}('${layerDefinition.name}');" : "loadSelectedLayer('${layerDefinition.name}');"}
    });

    $('#btnLayerSummaryShowInfo').click(function (e) {
        e.preventDefault();
        displayLayerSummaryInfo('${layerName}');
    });

    function displayLayerSummaryInfo(layerName) {
        showModal({
            url: "${createLink(controller: 'map', action:'layerInfoFragment')}?layerName=" + layerName,
            title: "Layer Details - " + layerName,
            height: 550,
            width: 600
        });
        return true;
    }


    function loadSelectedLayer(layerName) {
        if (layerName) {
            addLayer(layerName, false);
        }
    }


</script>