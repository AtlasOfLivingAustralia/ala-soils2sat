<div>
  <div style="height: 200px; overflow-y: scroll">
    <table class="table table-bordered table-striped">
      <tr>
        <td>Location</td>
        <td>${studyLocation.longitude}, ${studyLocation.latitude}</td>
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
        <td>From <sts:formatDateStr dateStr="${studyLocation.data.firstVisitDate}"/> to <sts:formatDateStr dateStr="${studyLocation.data.lastVisitDate}"/></td>
      </tr>
      <tr>
        <td>Sampling Units (${studyLocation.data.numSamplingUnits})</td>
        <td>
          <ul>
            <g:each in="${studyLocation.data.samplingUnitTypeList}" var="unit">
              <li>${unit}</li>
            </g:each>
          </ul>
        </td>
      </tr>

      %{--<g:each in="${results}" var="result">--}%
        %{--<tr>--}%
          %{--<td>${result.layername ?: result.field}</td>--}%
          %{--<td>${result.value}</td>--}%
        %{--</tr>--}%
      %{--</g:each>--}%
    </table>
  </div>
</div>