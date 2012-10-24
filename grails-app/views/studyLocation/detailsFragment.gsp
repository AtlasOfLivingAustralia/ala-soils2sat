<div>
  <H4>Study Location - ${studyLocationName}</H4>
  <div id="studyLocationSummaryPanel" class="well well-small">
    Retrieving Study Location data...
  </div>

  <g:if test="${appState?.selectedPlots?.find { it.name == studyLocationName }}">
    <button class="btn btn-small btn-warning" id="btnDeselectPlot">Remove from selection</button>
  </g:if>
  <g:else>
    <button class="btn btn-small btn-primary" id="btnSelectPlot">Add to selection</button>
  </g:else>
  <button class="btn btn-small" id="btnStudyLocationSummary">View Study Location Summary</button>

</div>

<script type="text/javascript">

  $("#btnSelectPlot").click(function(e) {
    e.preventDefault();
    selectPlot("${studyLocationName}");
    $.fancybox.close();
  });

  $("#btnDeselectPlot").click(function(e) {
    e.preventDefault();
    deselectPlot("${studyLocationName}");
    $.fancybox.close();
  });

  $("#btnStudyLocationSummary").click(function(e) {
    e.preventDefault();
    window.open("${createLink(controller:'studyLocation', action:'studyLocationSummary', params:[studyLocationName: studyLocationName])}", "StudyLocationSummary");
  });

  $(document).ready(function(e) {
    $.ajax("${createLink(controller:'studyLocation', action:'studyLocationDataFragment', params: [studyLocationName: studyLocationName])}").done(function(html) {
      $("#studyLocationSummaryPanel").html(html);
    });
  });

</script>