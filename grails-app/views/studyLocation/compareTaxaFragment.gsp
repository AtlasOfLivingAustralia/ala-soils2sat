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
        <g:each in="${appState.selectedPlots}" var="studyLocation">
          <th>${studyLocation.name}</th>
        </g:each>
      </tr>
    </thead>
    <tbody>
      <tr>
        <g:each in="${appState.selectedPlots}" var="studyLocation">
          <td class="${params.diffMode}">
            <g:each in="${results[studyLocation.name]}" var="taxaName">
            ${taxaName}<br/>
            </g:each>
          </td>
        </g:each>
      </tr>
    </tbody>
  </table>
</div>
