<div>
    <h5>Study location ${studyLocationName}</h5>
    <g:if test="${studyLocation}">
        <table>
            <tr>
                <td><strong>Visit&nbsp;dates</strong></td>
                <td><sts:formatDateStr date="${studyLocation.firstVisitDate}"/> - <sts:formatDateStr date="${studyLocation.lastVisitDate}"/></td>
            </tr>
            <tr>
                <td><strong>Position</strong></td>
                <td>${studyLocation.longitude}, ${studyLocation.latitude}</td>
            </tr>
            <tr>
                <td style="vertical-align: top"><strong>Sampling&nbsp;Units</strong></td>
                <td style="vertical-align: top">${studyLocation.samplingUnits?.collect({ it.description })?.join(", ")}</td>
            </tr>


        </table>
    </g:if>
    <g:else>
        Study location details could not be retrieved!
    </g:else>
</div>