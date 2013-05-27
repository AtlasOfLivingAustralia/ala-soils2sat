<g:set var="elementId" value="${java.util.UUID.randomUUID().toString()}" />
<div class="visualisationContent" id="${elementId}">
    <sts:spinner />
</div>

<g:if test="${!colors}">
    <g:set var="colors" value="['#99B958', '#4E80BB', '#BD4E4C']" />
</g:if>

<gvisualization:pieCoreChart dynamicLoading="true" name="chartv${name}" elementId="${elementId}" columns="${columns}" data="${data}" title="${title}" colors="${colors}" is3D="${true}" />
