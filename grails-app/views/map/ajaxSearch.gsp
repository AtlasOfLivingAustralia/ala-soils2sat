<div>
  <g:if test="${!results}">
    Your search returned no results.
  </g:if>
  <g:else>
    <div>
      ${results.size()} Study Locations found
    </div>
    <div class="well well-small" style="height: 245px; overflow-y: scroll;">
      <table class="table table-striped table-hover">
      <g:each in="${results}" var="result">
        <tr>
          <td class="plotSearchResultRow" plotName="${result.siteName}">${result.siteName} (${result.longitude}, ${result.latitude})</td>
          <td><button class="button btn-mini selectSearchResult pull-right" plotName="${result.siteName}">select</button></td>
        </tr>
      </g:each>
      </table>
    </div>
    <div>
      <button id="btnSelectAllSearchResults" class="btn btn-small btn-warning pull-right">Select all</button>
    </div>
  </g:else>
</div>

<script type="text/javascript">

  $(".selectSearchResult").click(function(e) {
    e.preventDefault();
    var plotName = $(this).attr("plotName");
    if (plotName) {
      selectPlot(plotName);
    }
  });

  $(".plotSearchResultRow").mouseover(function(e) {
    var plotName = $(this).attr("plotName");
    if (plotName) {
      showPlotHover(plotName);
    }
  }).mouseout(function(e) {
      hidePlotHover();
  });

  $("#btnSelectAllSearchResults").click(function(e) {
    var plotNames = [];
    $(".selectSearchResult").each(function() {
      var plotName = $(this).attr("plotName");
      if (plotName) {
        plotNames.push(plotName);
      }
    });
    selectPlots(plotNames);
  });

</script>