<g:if test="${data}">
    <g:set var="elementId" value="${UUID.randomUUID().toString().replaceAll("-", "_")}" />
    <div class="visualisationContent" id="${elementId}">
        <sts:spinner />
    </div>

    <script type="text/javascript">

        function shimHandler${name}() {
            <g:if test="${selectHandler}">
                if (${selectHandler}) {
                    ${selectHandler}(${name});
                }
            </g:if>
        }

    </script>

    <g:if test="${!stacked}">
        <g:set var="stacked" value="${false}" />
    </g:if>


    <gvisualization:barCoreChart dynamicLoading="true" name="${name}" elementId="${elementId}" columns="${columns}" data="${data}" title="${title}" colors="${colors}" isStacked="${true}" select="shimHandler${name}"
                                 vAxis="${new Expando([title: vAxisTitle ?: ''])}"
                                 hAxis="${new Expando([title: hAxisTitle ?: ''])}"
    />

</g:if>
<g:else>
    <div class="muted noDataRecordedChart">
        <strong class="muted">${title}</strong>
        <div>
            No data recorded
        </div>
    </div>
</g:else>
