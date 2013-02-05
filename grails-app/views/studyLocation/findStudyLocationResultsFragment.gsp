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
          <td class="studyLocationSearchResultRow" studyLocationName="${result.siteName}">${result.siteName} (${result.latitude}, ${result.latitude})</td>
          <td>
            <g:set var="selected" value="${!appState.containsPlot(result.siteName)}"/>
              <button style="display:${selected ? 'block' : 'none'}" class="btn btn-mini selectSearchResult pull-right" studyLocationName="${result.siteName}">Select</button>
              <button style="display:${selected ? 'none' : 'block'}" class="btn btn-mini btn-warning deselectSearchResult pull-right" studyLocationName="${result.siteName}">Deselect</button>
          </td>
        </tr>
      </g:each>
      </table>
    </div>
    <div>
      <button id="btnSelectAllSearchResults" class="btn btn-small btn-info pull-right">Select all</button>
      <button id="btnClearCurrentSelection" class="btn btn-small pull-right" style="margin-right:5px">Clear all selected</button>
    </div>
  </g:else>
</div>

<script type="text/javascript">

  $(".selectSearchResult").click(function(e) {
    e.preventDefault();
    var studyLocationName = $(this).attr("studyLocationName");
    if (studyLocationName) {
      selectPlot(studyLocationName);
      $(this).css("display", "none");
      $(this).siblings(".deselectSearchResult").css("display", "block");
    }
  });

  $(".deselectSearchResult").click(function(e) {
    e.preventDefault();
    var studyLocationName = $(this).attr("studyLocationName");
    if (studyLocationName) {
      deselectPlot(studyLocationName);
      $(this).css("display", "none");
      $(this).siblings(".selectSearchResult").css("display", "block");
    }

  });

  $(".studyLocationSearchResultRow").mouseover(function(e) {
    var studyLocationName = $(this).attr("studyLocationName");
    if (studyLocationName) {
      showPlotHover(studyLocationName);
    }
  }).mouseout(function(e) {
      hidePlotHover();
  });

  $("#btnSelectAllSearchResults").click(function(e) {
    var studyLocationNames = [];
    $(".selectSearchResult").each(function() {
      var studyLocationName = $(this).attr("studyLocationName");
      if (studyLocationName) {
        studyLocationNames.push(studyLocationName);
      }
    });
    selectPlots(studyLocationNames, function() { doSearch(); });
  });

  $("#btnClearCurrentSelection").click(function(e) {
    clearSelectedPlots(function() { doSearch() });
  });

</script>