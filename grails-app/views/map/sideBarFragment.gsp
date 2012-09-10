<div class="well well-small">
  <h4>Selected Study Locations</h4>
  <p>
    <button class="btn btn-small" id="btnFindPlot">Find Study Location <i class="icon-search"></i></button>
    <button class="btn btn-small" id="btnClearSelection">Remove All</button>
  </p>
  %{--<br /></br/>--}%

  <table class="table table-bordered table-condensed table-striped">
    <g:if test="${userInstance.selectedPlots}">
        <g:each in="${userInstance.selectedPlots}">
          <tr>
            <td><a class="plotDetailsLink" href="#" plotName="${it}">${it}</a><button class="btn btn-mini pull-right btnRemoveSelectedPlot" plotName="${it}" title="Remove study location"><i class="icon-trash"/></button></td>
          </tr>
        </g:each>
    </g:if>
    <g:else>
      <tr>
        <td>No study locations have been selected</td>
      </tr>
    </g:else>
  </table>
  <p>
    <button class="btn btn-small btn-primary">Compare</button>
    <button class="btn btn-small">Export</button>
  </p>

</div>

<div id="layersPanel" class="well well-small">
    <h4>Environmental layers</h4>
    <div id="layersTable" style="overflow-y: scroll;">
      <g:if test="${userInstance.layers}">
      <table class="table table-striped table-condensed">

        <g:each in="${userInstance.layers}">
          <tr>
            <td>${it}</td>
            %{--<td><button class="btn btn-mini pull-right" layerName="${it}"><i class="icon-eye-open" /></button></td>--}%
            <td><button class="btn btn-mini pull-right btnRemoveLayer" layerName="${it}"><i class="icon-trash"/></button></td>
          </tr>
        </g:each>
      </table>
      </g:if>
      <g:else>
        No layers have been added
      </g:else>
    </div>
    <p>
      <button id="btnLayerAdd" class="btn btn-small btn-success">Add Layer <i class="icon-plus icon-white"/></button>
    </p>
</div>

<script type="text/javascript">
  $(".btnRemoveSelectedPlot").click(function(e) {
    e.preventDefault();
    var plotName = $(this).attr("plotName");
    deselectPlot(plotName);
  });

  $(".plotDetailsLink").click(function(e) {
    e.preventDefault();
    var plotName = $(this).attr("plotName");
    showPlotDetails(plotName);
  });

  $("#btnClearSelection").click(function(e) {
    e.preventDefault();
    clearSelectedPlots();
  });

  $("#btnFindPlot").click(function(e) {
    e.preventDefault();
    findPlot();
  });

  $("#btnLayerAdd").click(function(e) {
    e.preventDefault();
    addLayerClicked();
  });

  $(".btnRemoveLayer").click(function(e) {
    e.preventDefault();
    var layerName = $(this).attr("layerName");
    removeLayer(layerName);
  });

  function resizeLayerPanel() {
    var mapHeight = $("#mapContent").height() + $("#mapContent").offset().top;
    var panelTop = $("#layersPanel").offset().top;
    var height = mapHeight - 20 - panelTop;
    $("#layersPanel").height(height);
    $("#layersTable").height(height - 70);
  }

  $(window).resize(function(e) {
    resizeLayerPanel();
  });

  resizeLayerPanel();

</script>
