<style>

  .intersect {
    color: green;
  }

  .inverseIntersect {
    color: red;
  }

</style>
<div>
  <table class="table table-striped table-condensed">
    <thead>
      <tr>
        <g:each in="${appState.selectedPlotNames}" var="studyLocation">
          <th>${studyLocation}</th>
        </g:each>
      </tr>
    </thead>
    <tbody>
      <tr>
        <g:each in="${appState.selectedPlotNames}" var="studyLocation">
          <td class="${params.diffMode}">
            <g:each in="${results[studyLocation]}" var="taxaName">
            ${taxaName}<br/>
            </g:each>
          </td>
        </g:each>
      </tr>
    </tbody>
  </table>
</div>
