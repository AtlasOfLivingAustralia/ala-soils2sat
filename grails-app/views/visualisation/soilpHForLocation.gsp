<g:set var="elementId" value="${java.util.UUID.randomUUID().toString()}" />
<div class="visualisationContent" id="${elementId}">
    <sts:spinner />
</div>

<gvisualization:barCoreChart dynamicLoading="true" name="chartvPH" elementId="${elementId}" columns="${columns}" data="${data}" title="Soil pH" colors="${colors}" isStacked="${true}" />
