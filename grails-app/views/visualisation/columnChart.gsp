<g:if test="${data}">
    <g:set var="elementId" value="${UUID.randomUUID().toString().replaceAll("-", "_")}" />
    <div class="visualisationContent" id="${elementId}">
        <sts:spinner />
    </div>

    <gvisualization:columnCoreChart dynamicLoading="true" name="columnChar_${name}" elementId="${elementId}" columns="${columns}" data="${data}" title="${title}" colors="${colors}" isStacked="${stacked}"
                                    hAxis="${new Expando([slantedText: true, slantedTextAngle: 90, textStyle:new Expando([fontSize:9])])}"
                                    legend="none"
    />
</g:if>
<g:else>
    <div>No layer data found</div>
</g:else>
