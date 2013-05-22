<g:set var="elementId" value="${java.util.UUID.randomUUID().toString()}" />
<div class="visualisationContent" id="${elementId}">
    <sts:spinner />
</div>

<g:set var="colors" value="['#99B958', '#4E80BB', '#BD4E4C']" />

<gvisualization:pieCoreChart dynamicLoading="true" name="chartvPlantBreakDownByLocation" elementId="${elementId}" columns="${columns}" data="${data}" title="Plant Species Breakdown by Source" colors="${colors}" is3D="${true}" />
