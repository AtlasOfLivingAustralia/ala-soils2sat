<g:set var="elementId" value="${java.util.UUID.randomUUID().toString()}" />
<div class="visualisationContent" id="${elementId}">
    <sts:spinner />
</div>

<gvisualization:pieCoreChart dynamicLoading="true" name="chartvWeedVNonWeed" elementId="${elementId}" columns="${columns}" data="${data}" title="Weed / Non-Weed Species Abundance % Breakdown" colors="${colors}" is3D="${true}" />
