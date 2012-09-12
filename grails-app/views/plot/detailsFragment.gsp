<div>
  <H4>Study Location Summary - ${plotName}</H4>

  <div id="plotSummaryPanel" class="well well-small">
    Retrieving plot data...
  </div>

  <g:if test="${userInstance.selectedPlots?.contains(plotName)}">
    <button class="btn btn-small btn-warning" id="btnDeselectPlot">Remove from selection</button>
  </g:if>
  <g:else>
    <button class="btn btn-small btn-primary" id="btnSelectPlot">Add to selection</button>
  </g:else>

</div>

<script type="text/javascript">

  $("#btnSelectPlot").click(function(e) {
    e.preventDefault();
    selectPlot("${plotName}");
    $.fancybox.close();
  });

  $("#btnDeselectPlot").click(function(e) {
    e.preventDefault();
    deselectPlot("${plotName}");
    $.fancybox.close();
  });

  $(document).ready(function(e) {
    $.ajax("${createLink(controller:'plot', action:'plotDataFragment', params: [plotName: plotName])}").done(function(html) {
      $("#plotSummaryPanel").html(html);
    });
  });

</script>