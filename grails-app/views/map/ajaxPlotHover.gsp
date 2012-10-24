<div>
  <h5>Study location ${studyLocationName}</h5>
  <g:if test="${studyLocation}">
    <table>
      <tr>
        <td>Date</td>
        <td>${studyLocation.data.lastVisitDate}</td>
      </tr>
      <tr>
        <td>Position</td>
        <td>${studyLocation.longitude}, ${studyLocation.latitude}</td>
      </tr>
    </table>
  </g:if>
  <g:else>
    Study location details could not be retrieved!
  </g:else>
</div>