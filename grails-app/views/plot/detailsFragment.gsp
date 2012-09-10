<div>
  <H3>Study Location Summary - ${plotName}</H3>

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

</script>