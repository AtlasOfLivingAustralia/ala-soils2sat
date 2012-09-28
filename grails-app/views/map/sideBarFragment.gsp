<style type="text/css">

  td.toolButtonCell {
    width:20px;
    padding: 4px 2px;
  }

</style>
<div class="well well-small">
  <table style="width: 100%">
    <tr>
      <td>
        <h5>Selected Study Locations
          <g:if test="${appState?.selectedPlots}">
            (${appState.selectedPlots?.size()})
          </g:if>
        </h5>
      </td>
      <td style="text-align: right">
        <button id="btnTogglePlotSelected" class="btn btn-mini ${appState.plotOnlySelectedLocations ? 'active' : ''}">Show only selected locations on map</button>
      </td>
    </tr>
  </table>
  <p>
    <button class="btn btn-small btn-info" id="btnFindPlot">Find Study Location <i class="icon-search icon-white"></i></button>
  </p>
  %{--<br /></br/>--}%

  <div style="max-height: 200px; overflow-y: auto; margin-bottom: 5px">
    <table class="table table-bordered table-condensed table-striped" >
      <g:if test="${appState?.selectedPlots}">
          <g:each in="${appState?.selectedPlots}">
            <tr>
              <td><a class="plotDetailsLink" href="#" plotName="${it.name}">${it.name}</a><button class="btn btn-mini pull-right btnRemoveSelectedPlot" plotName="${it.name}" title="Remove study location"><i class="icon-trash"/></button></td>
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
        <td style="width: 90px;">
          <button id="btnLayerRemoveAll" class="btn btn-mini btn-danger pull-right">Remove All&nbsp;<i class="icon-trash icon-white"/></button>
        </td>
        <td style="width: 90px;">
          <button id="btnLayerAdd" class="btn btn-mini btn-success pull-right">Add Layer&nbsp;<i class="icon-plus icon-white"/></button>
        </td>
      </tr>
    </table>

    <div id="layersTable" style="overflow-y: scroll;">
      <g:if test="${appState?.layers}">
      <table class="table table-striped table-condensed">

        <g:each in="${appState?.layers}">
          <tr>
            <td><a href="#" class="btnLayerInfo" layerName="${it.name}">${it.name}</a></td>
            <td class="toolButtonCell">
              <button class="btn btn-mini btnToggleLayerVisibility ${it.visible ? 'active' : ''}" layerName="${it.name}" title="Show/Hide this layer in the map"><i class="icon-eye-open" /></button></td>
            </td>

            <td class="toolButtonCell">
              <button class="btn btn-mini btnLayerInfo" style="margin-right:5px" title="Display layer information" layerName="${it.name}" title="Show information about this layer"><i class="icon-info-sign"/></button>
            </td>
            <td class="toolButtonCell">
              <button class="btn btn-mini btnRemoveLayer" layerName="${it.name}" title="Remove layer from the list"><i class="icon-trash"/></button>
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

  $("#btnTogglePlotSelected").click(function(e) {
    e.preventDefault();
    var currentState = $(this).hasClass('active');
    if ($(this).hasClass('active')) {
      $(this).removeClass('active');
    } else {
      $(this).addClass('active');
    }

    $.ajax("${createLink(controller: 'plot', action: 'ajaxSetPlotSelectedOnly')}?plotSelected=" + !currentState).done(function(e) {
      refreshStudyLocationPoints();
    });

  });

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

  $("#btnLayerRemoveAll").click(function(e) {
    e.preventDefault();
    clearAllLayers();
  });

  $(".btnRemoveLayer").click(function(e) {
    e.preventDefault();
    var layerName = $(this).attr("layerName");
    removeLayer(layerName);
  });

  $(".btnToggleLayerVisibility").click(function(e) {
    var element = $(this);
    var layerName = element.attr("layerName");
    if (layerName) {
      if ($(this).hasClass("active")) {
        $.ajax("${createLink(controller: 'map', action: 'ajaxSetLayerVisibility')}?layerName=" + layerName + '&visibility=false').done(function(e) {
          unloadWMSLayer(layerName);
          element.removeClass("active");
        });
      } else {
        $.ajax("${createLink(controller: 'map', action: 'ajaxSetLayerVisibility')}?layerName=" + layerName + '&visibility=true').done(function(e) {
          loadWMSLayer(layerName);
          element.addClass("active");
        });
      }
    }
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
