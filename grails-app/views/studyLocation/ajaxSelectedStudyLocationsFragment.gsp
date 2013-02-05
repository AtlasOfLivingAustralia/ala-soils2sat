<table class="table table-bordered table-condensed table-striped">
    <g:if test="${appState?.selectedPlots}">
        <g:each in="${appState?.selectedPlots}">
            <tr>
                <td><a class="studyLocationDetailsLink" href="#" studyLocationName="${it.name}">${it.name}</a><button class="btn btn-mini pull-right btnRemoveSelectedPlot" studyLocationName="${it.name}" title="Remove study location"><i class="icon-trash"/>
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
