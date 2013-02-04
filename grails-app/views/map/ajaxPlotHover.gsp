<div>
  <h5>Study location ${studyLocationName}</h5>
  <g:if test="${studyLocation}">
    <table>
      <tr>
        <td>First visit</td>
        <td><sts:formatDateStr date="${studyLocation.firstVisitDate}"/></td>
      </tr>
        <tr>
          <td>Last visit</td>
          <td><sts:formatDateStr date="${studyLocation.lastVisitDate}"/></td>
        </tr>
      <tr>
        <td>Position (Lat,Long)</td>
        <td>${studyLocation.longitude}, ${studyLocation.latitude}</td>
      </tr>
        <tr>
          <td>Position (UTM)</td>
          <td>${studyLocation.data.easting}, ${studyLocation.data.northing} (${studyLocation.data.zone as Integer})</td>
        </tr>

    </table>
  </g:if>
  <g:else>
    Study location details could not be retrieved!
  </g:else>
</div>