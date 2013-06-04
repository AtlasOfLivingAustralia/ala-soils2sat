<style>

.intersect {
    color: green;
}

.inverseIntersect {
    color: red;
}

</style>

<div>
    <div>
        <strong>Vouchered specimens recorded at selected Study Locations</strong>
        <br/>
        <small>
            <g:if test="${diffMode == 'none'}">
            </g:if>
            <g:if test="${diffMode == 'intersect'}">
                Only showing those taxa that occur at every selected study location
            </g:if>
            <g:if test="${diffMode == 'inverseIntersect'}">
                Only showing those taxa that occur at only one of the selected study locations
            </g:if>
        </small>
    </div>
    <table class="table table-striped table-condensed">
        <thead>
            <tr>
                <g:each in="${appState.selectedPlotNames}" var="studyLocation">
                    <th>${studyLocation}</th>
                </g:each>
            </tr>
        </thead>
        <tbody>
            <g:if test="${results.values().find { it.size() > 0 }}">
                <tr>
                    <g:each in="${appState.selectedPlotNames}" var="studyLocation">
                        <td class="${params.diffMode}">
                            <g:each in="${results[studyLocation]}" var="taxaName">
                                ${taxaName}<br/>
                            </g:each>
                        </td>
                    </g:each>
                </tr>
            </g:if>
            <g:else>
                <tr>
                    <td colspan="${appState.selectedPlotNames.size()}">No matching data found</td>
                </tr>
            </g:else>
        </tbody>
    </table>
</div>
