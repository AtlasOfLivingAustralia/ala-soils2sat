<div id="searchContainer" class="">
  %{--<h3>Find Study Location</h3>--}%
  <form class="form-horizontal" id="searchForm">
    <div class="control-group">
      <label class="control-label" for="searchText">Search</label>
      <div class="controls">
        <input type="text" id="searchText" placeholder="Search..." style="width:300px">
        <label class="checkbox" style="margin-top: 5px;">
          <input id="chkUseBox" type="checkbox"checked="checked"> Restrict results to visible portion of the map
        </label>
      </div>
    </div>

    <div class="control-group">
      <div class="controls">
        <button id="btnSearch" class="btn btn-small btn-primary">Search</button>
        <button id="btnCancelSearch" class="btn btn-small">Close</button>
      </div>
    </div>
  </form>
  <div id="searchResults">

  </div>
</div>

<script type="text/javascript">
  $("#btnCancelSearch").click(function(e) {
    e.preventDefault();
    $("#findPlot").modal('hide');
  });

  $("#btnSearch").click(function(e) {
    e.preventDefault();
    doSearch();
  });

  function doSearch() {
    var term = $("#searchText").val();

    if (term) {
      $("#searchResults").html("Searching...");
      var url = "${createLink(controller: 'studyLocation', action: 'findStudyLocationResultsFragment')}?q=" + term;
      var useBox = $("#chkUseBox").is(":checked");
      if (useBox) {
        var projWGS84 = new OpenLayers.Projection("EPSG:4326");
        var proj900913 = new OpenLayers.Projection("EPSG:900913");
        var extent = map.getExtent().transform(proj900913, projWGS84 );
        url += "&top=" + extent.top + "&left=" + extent.left + "&bottom=" + extent.bottom + "&right=" + extent.right;
      }

      $.ajax(url).done(function(html) {
        $("#searchResults").html(html);
      });
    } else {
      $("#searchResults").html("Please enter a search term ('*' for all)");
    }
  }

  $("#searchText").focus();

</script>