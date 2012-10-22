<table class="table table-striped table-condensed">
  <thead>
    <tr>
      <g:each in="${appState.selectedPlots}" var="plot">
        <th>${plot.name}</th>
      </g:each>
    </tr>
  </thead>
  <tbody>
    <tr>
      <g:each in="${appState.selectedPlots}" var="plot">
        <td>
          <g:each in="${results[plot.name]}" var="taxaName">
          ${taxaName}<br/>
          </g:each>
        </td>
      </g:each>
    </tr>
  </tbody>
</table>
