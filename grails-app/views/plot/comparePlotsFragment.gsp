<legend>
<table style="width: 100%">
  <tr>
    <td>Compare Study Locations (${appState?.selectedPlots?.size()})</td>
    <td style="text-align: right">
      <button id="btnCompareExport" class="btn btn-small">Export as CSV</button>
    </td>
  </tr>
</table>
</legend>
<g:if test="${appState?.layers?.size() >= 1 && appState?.selectedPlots?.size() > 1}">
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
      <g:each in="${results.fieldNames}" var="fieldName">
        <tr>
          <td>${fieldName}</td>
          <g:each in="${appState?.selectedPlots}" var="plot">
            <td>${results.data[plot.name][fieldName]}</td>
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
<script type="text/javascript">
  $("#btnCompareExport").click(function(e) {
    e.preventDefault();
    location.href = "${createLink(controller: 'plot', action:'exportComparePlots')}";
  });
</script>