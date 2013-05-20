<g:set var="elementId" value="${UUID.randomUUID().toString().replaceAll("-", "_")}" />
<div class="visualisationContent" id="${elementId}">
    <sts:spinner />
</div>

<gvisualization:barCoreChart dynamicLoading="true" name="chartEC" elementId="${elementId}" columns="${columns}" data="${data}" title="Soil EC" colors="${[colors]}" isStacked="${true}" />
