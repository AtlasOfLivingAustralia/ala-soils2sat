<g:if test="${data}">
    <g:set var="elementId" value="${UUID.randomUUID().toString().replaceAll("-", "_")}" />
    <div class="visualisationContent" id="${elementId}">
        <sts:spinner />
    </div>

    <gvisualization:columnCoreChart dynamicLoading="true" name="chartDistinctSpecies" elementId="${elementId}" columns="${columns}" data="${data}" title="Number of Distinct Species" colors="${colors}" isStacked="${true}" hAxis="${new Expando([slantedText: true, slantedTextAngle: 90, textStyle:new Expando([fontSize:9])])}" legend="none" />
</g:if>
<g:else>
    <div>No data recorded</div>
</g:else>
