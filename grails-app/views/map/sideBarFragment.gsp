<div class="well well-small">
  <table style="width: 100%">
    <tr>
      <td>
        <h5>Selected Study Locations
          <g:if test="${userInstance.selectedPlots}">
            (${userInstance.selectedPlots.size()})
          </g:if>
        </h5>
      </td>
    </tr>
  </table>
  <p>
    <button class="btn btn-small btn-info" id="btnFindPlot">Find Study Location <i class="icon-search icon-white"></i></button>
  </p>
  %{--<br /></br/>--}%

  <div style="max-height: 200px; overflow-y: auto; margin-bottom: 5px">
    <table class="table table-bordered table-condensed table-striped" >
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
  </div>
  <p style="margin-top: 15px">
    <button id="btnComparePlots" class="btn btn-small btn-primary">Compare</button>
    <button class="btn btn-small">Export</button>
    <button class="btn btn-small" id="btnClearSelection">Remove All</button>
  </p>

</div>

<div id="layersPanel" class="well well-small">
    <table style="width: 100%">
      <tr>
        <td><h5>Environmental layers</h5></td>
        <td><button id="btnLayerAdd" class="btn btn-mini btn-success pull-right">Add Layer <i class="icon-plus icon-white"/></button></td>
      </tr>
    </table>

    <div id="layersTable" style="overflow-y: scroll;">
      <g:if test="${userInstance.layers}">
      <table class="table table-striped table-condensed">

        <g:each in="${userInstance.layers}">
          <tr>
            <td><a href="#" class="btnLayerInfo" layerName="${it}">${it}</a></td>
            %{--<td><button class="btn btn-mini pull-right" layerName="${it}"><i class="icon-eye-open" /></button></td>--}%
            <td>
              <button class="btn btn-mini pull-right btnLayerInfo" style="margin-right:5px" title="Display layer information" layerName="${it}"><i class="icon-info-sign"/></button>
            </td>
            <td>
              <button class="btn btn-mini pull-right btnRemoveLayer" layerName="${it}" title="Remove layer"><i class="icon-trash"/></button>
            </td>
          </tr>
        </g:each>
      </table>
      </g:if>
      <g:else>
        No layers have been added
      </g:else>
    </div>
</div>

<script type="text/javascript">

  $("#btnComparePlots").click(function(e) {
    e.preventDefault();
    compareSelectedPlots();
  });

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

  $(".plotDetailsLink").mouseover(function(e) {
    e.preventDefault();
    var plotName = $(this).attr("plotName");
    showPlotHover(plotName);
  });

  $(".plotDetailsLink").mouseout(function(e) {
    e.preventDefault();
    var plotName = $(this).attr("plotName");
    hidePlotHover(plotName);
  });

  $(".btnLayerInfo").click(function(e) {
    var layerName = $(this).attr("layerName");
    displayLayerInfo(layerName);
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
    $("#layersTable").height(height - 40);
  }

  $(window).resize(function(e) {
    resizeLayerPanel();
  });

  resizeLayerPanel();

</script>
