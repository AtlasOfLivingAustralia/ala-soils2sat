<h4>Compare Study Locations (${userInstance.selectedPlots?.size()})</h4>

<table class="table table-striped table-condensed table-bordered" style="max-height: 540px; width:780px; overflow: scroll;">
  <thead>
    <tr>
      <th></th>
      <g:each in="${userInstance.selectedPlots}" var="plotName">
        <th>${plotName}</th>
      </g:each>
    </tr>
  </thead>
  <tbody>
    <g:each in="${userInstance.layers}" var="layerName">
      <tr>
        <td>${layerName}</td>
        <g:each in="${userInstance.selectedPlots}" var="plotName">
          <td>${results[plotName][layerName]}</td>
        </g:each>
      </tr>
    </g:each>
  </tbody>
</table>