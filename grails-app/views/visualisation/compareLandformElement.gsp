<g:set var="elementId" value="${java.util.UUID.randomUUID().toString()}" />
<div class="visualisationContent" id="${elementId}">
    <sts:spinner />
</div>

<gvisualization:pieCoreChart dynamicLoading="true" name="chartvCompareLanformElement" elementId="${elementId}" columns="${columns}" data="${data}" title="Study Location & Breakdown by Landform Element" is3D="${true}" />
