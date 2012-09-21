<h4>Compare Plots</h4>
<table class="table table-bordered" style="max-height: 540px; width:780px; overflow: scroll;">
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
          <td></td>
        </g:each>
      </tr>
    </g:each>
  </tbody>
</table>