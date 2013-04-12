<table class="table table-bordered table-condensed table-striped">
    <g:if test="${appState?.selectedVisits}">
        <g:each in="${appState?.selectedVisits}">
            <tr studyLocationName="${it.studyLocationName}" studyLocationVisitId="${it.studyLocationVisitId}">
                <td>
                    <a class="studyLocationVisitDetailsLink" href="${createLink(controller:'studyLocation', action:'studyLocationVisitSummary', params:[studyLocationName:it.studyLocationName,studyLocationVisitId: it.studyLocationVisitId])}"><sts:formatVisitLabel studyLocationName="${it.studyLocationName}" studyLocationVisitId="${it.studyLocationVisitId}"/></a>
                    <button class="btn btn-mini pull-right btnRemoveSelectedPlot" title="Remove study location visit"><i class="icon-remove"/></button>
                </td>
            </tr>
        </g:each>
    </g:if>
    <g:else>
        <tr>
            <td>No study location visits have been selected</td>
        </tr>
    </g:else>

    <g:if test="${appState?.selectedPlotNames}">
        <g:each in="${appState?.selectedPlotNames}">
            <tr studyLocationName="${it}">
                <td><a class="studyLocationDetailsLink" href="${createLink(controller:'studyLocation', action:'studyLocationSummary', params:[studyLocationName:it])}">${it}</a><button class="btn btn-mini pull-right btnRemoveSelectedPlot" title="Remove study location"><i class="icon-remove"/>
                </button></td>
            </tr>
        </g:each>
    </g:if>
    <g:else>
        <tr>
            <td>No study locations have been selected</td>
        </tr>
    </g:else>
</table>

<script type="text/javascript">

</script>
