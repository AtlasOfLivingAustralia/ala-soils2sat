<div>
    <h5>Study location ${studyLocationName}</h5>
    <g:if test="${studyLocation}">
        <table>
            <tr>
                <td>Visit dates</td>
                <td><sts:formatDateStr date="${studyLocation.firstVisitDate}"/> - <sts:formatDateStr date="${studyLocation.lastVisitDate}"/></td>
            </tr>
            <tr>
                <td>Position</td>
                <td>${studyLocation.longitude}, ${studyLocation.latitude}</td>
            </tr>
            %{--<tr>--}%
                %{--<td>Position (UTM)</td>--}%
                %{--<td>${studyLocation.easting}, ${studyLocation.northing} (${studyLocation.mgaZone as Integer})</td>--}%
            %{--</tr>--}%
            <tr>
                <td>Observers</td>
                <td>${studyLocation.observers?.join(", ")}</td>
            </tr>
            <tr>
                <td>Sampling Units</td>
                <td>${studyLocation.samplingUnits?.collect({ it.description })?.join(", ")}</td>
            </tr>


        </table>
    </g:if>
    <g:else>
        Study location details could not be retrieved!
    </g:else>
</div>