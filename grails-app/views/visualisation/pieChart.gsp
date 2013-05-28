<g:set var="elementId" value="${java.util.UUID.randomUUID().toString()}" />
<div class="visualisationContent" id="${elementId}">
    <sts:spinner />
</div>

<g:if test="${!colors}">
    <g:set var="colors" value="['#4E80BB', '#BD4E4C', '#98B856','#7E619E','#4AA8C2','#F39344','#3E6899','#9B403C','#7D9645','#685083','#3A8BA1','#C97A36']" />
</g:if>

<script type="text/javascript">

    function shimHandler${name}() {
        <g:if test="${selectHandler}">
            if (${selectHandler}) {
                ${selectHandler}(${name});
            }
        </g:if>
    }

</script>

<gvisualization:pieCoreChart dynamicLoading="true" name="${name}" elementId="${elementId}" columns="${columns}" data="${data}" title="${title}" colors="${colors}" is3D="${true}" select="shimHandler${name}" />
