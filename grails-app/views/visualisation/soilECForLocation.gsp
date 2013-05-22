<g:if test="${data}">
    <g:set var="elementId" value="${UUID.randomUUID().toString().replaceAll("-", "_")}" />
    <div class="visualisationContent" id="${elementId}">
        <sts:spinner />
    </div>

    <gvisualization:barCoreChart dynamicLoading="true" name="chartEC" elementId="${elementId}" columns="${columns}" data="${data}" title="Soil EC" colors="${[colors]}" isStacked="${true}" />
</g:if>
<g:else>
    <div>No EC data recorded</div>
</g:else>
