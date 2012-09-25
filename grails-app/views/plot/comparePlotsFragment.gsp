<h4>Compare Study Locations (${appState?.selectedPlots?.size()})</h4>
<g:if test="${appState?.layers && appState?.selectedPlots?.size() > 1}">
  <table class="table table-striped table-condensed table-bordered" style="max-height: 540px; width:780px; overflow: scroll;">
    <thead>
      <tr>
        <th></th>
        <g:each in="${appState.selectedPlots}" var="plot">
          <th>${plot.name}</th>
        </g:each>
      </tr>
    </thead>
    <tbody>
      <g:each in="${appState?.layers}" var="layer">
        <tr>
          <td>${layer.name}</td>
          <g:each in="${appState?.selectedPlots}" var="plot">
            <td>${results[plot.name][layer.name]}</td>
          </g:each>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <p>You must first select at least one environmental layer, and two or more study locations before using this feature.
  </p>
</g:else>