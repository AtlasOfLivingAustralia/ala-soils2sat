<div>
  <div style="height: 250px; overflow-y: scroll">
    <table class="table table-bordered table-striped">
      <tr>
        <td>Location (Lat, Long)</td>
        <td>${studyLocation.longitude}, ${studyLocation.latitude}</td>
      </tr>
        <tr>
          <td>Location (UTM)</td>
          <td>${studyLocation.data.easting}, ${studyLocation.data.northing} (Zone ${studyLocation.data.zone as Integer})</td>
        </tr>
      <tr>
        <td>Bioregion Name</td>
        <td>${studyLocation.data.bioregionName}</td>
      </tr>
      <tr>
        <td>Landform Pattern</td>
        <td>${studyLocation.data.landformPattern}</td>
      </tr>
      <tr>
        <td>Landform Element</td>
        <td>${studyLocation.data.landformElement}</td>
      </tr>
      <tr>
        <td>Visits (${studyLocation.data.numVisits})</td>
        <td>Initial visit date: <sts:formatDateStr date="${studyLocation.firstVisitDate}"/><br/>
            Lastest visit date: <sts:formatDateStr date="${studyLocation.lastVisitDate}"/>
        </td>
      </tr>
      <tr>
        <td>Sampling Methods performed<br/>at this site location</td>
        <td>
          <ul>
            <g:each in="${studyLocation.data.samplingUnitTypeList}" var="unit">
              <li><sts:formatSamplingUnitColumn code="${unit}"/></li>

            </g:each>
          </ul>
        </td>
      </tr>
    </table>
  </div>
</div>