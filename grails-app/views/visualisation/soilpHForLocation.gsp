<g:if test="${data}">
    <g:set var="elementId" value="${java.util.UUID.randomUUID().toString()}" />
    <div class="visualisationContent" id="${elementId}">
        <sts:spinner />
    </div>

    <gvisualization:barCoreChart dynamicLoading="true" name="chartvPH" elementId="${elementId}" columns="${columns}" data="${data}" title="Soil pH" colors="${colors}" isStacked="${true}" />
</g:if>
<g:else>
    <div>No pH data recorded</div>
</g:else>
