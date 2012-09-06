<!doctype html>
<html>
	<head>
    <r:require module='jqueryui' />
    <r:require module='openlayers' />
    <r:require module='fancybox' />

		<meta name="layout" content="soils2sat"/>
		<title>Soils to Satellites</title>
		<style type="text/css" media="screen">
      #content {
        top: 62px;
        bottom: 80px;
        position: absolute;
        width: 100%;
      }

      #mapContent {
        height: 600px;
      }

      #layersContent {
        float: right;
        display: inline-block;
        width: 200px;
      }

      .info-popup {
        position: absolute;
        bottom: 100px;
        right: 10px;
        width: 200px;
        height:100px;
        background: #393939;
        border: 3px solid #393939;
        color: white;
        border-radius: 10px;
        padding: 10px;
        z-index: 9999;
      }

      .info-popup h4 {
        margin: 2px;
      }

      .olLayerGooglePoweredBy {
        display: none;
      }

		</style>

    <script src="http://maps.google.com/maps/api/js?key=AIzaSyDAqE0sOMHNienA0zFIUMlY53ztEDiv0d8&sensor=true" type="text/javascript"></script>
    <link rel="stylesheet" href="${resource(dir: 'js/openlayers/theme/default', file: 'style.css')}" type="text/css">

	</head>
	<body>
    <script type="text/javascript">

      $(window).resize(function(e) {
        resizeMap();
      });

      function resizeMap() {
        var height = $(window).height() - (63 + 90);
         $("#mapContent").height(height);
      }

      $(document).ready( function (e) {

        resizeMap();

        initMap();

        $("#btnLayerAdd").click(function(e) {
          e.preventDefault();
          $("#addLayerLink").click();
          return true;
        });

        $("#addLayerLink").fancybox({
            beforeLoad: function() {
              $.ajax("${createLink(controller: 'map', action:'addLayerFragment')}").done(function(data) {
                $("#addLayerContent").html(data);
              });
            }
        });

        $("#plotDetailsLink").fancybox({
          beforeLoad: function() {
            var plotName = $("#plotDetailsContent").attr("plotName");
            $.ajax("${createLink(controller: 'plot', action:'detailsFragment')}?plotName=" + plotName).done(function(data) {
              $("#plotDetailsContent").html(data);
            });
          }

        });

      });

      function loadWMSLayer(name) {

        var wmsLayer = new OpenLayers.Layer.WMS(name, "${grailsApplication.config.spatialPortalRoot}/geoserver/gwc/service/wms/reflect", {
            layers : 'ALA:' + name,
            srs : 'EPSG:900913',
            format : 'image/png',
            transparent : true
        });

        // wmsLayer.visibility = false;
        map.addLayer(wmsLayer);
        var plots = map.getLayer("Plots");
        if (plots) {
          map.removeLayer(plots);
          map.addLayer(plots);
        }
      }

      function initMap() {

        infoMessage("Initialising map...");

        var options = {
          projection: "EPSG:900913",
          maxExtent: new OpenLayers.Bounds(-20037508, -20037508, 20037508, 20037508),
          units:"m",
          controls: [
            new OpenLayers.Control.Navigation({ mouseWheelOptions: {interval: 50, cumulative: false}, zoomBoxEnabled: true } ),
            new OpenLayers.Control.ArgParser(),
            new OpenLayers.Control.PanZoom(),
            new OpenLayers.Control.PanZoomBar()
          ]
        };

        map = new OpenLayers.Map('mapContent', options);

        map.addControl(new OpenLayers.Control.LayerSwitcher());

        var gphy = new OpenLayers.Layer.Google(
            "Google Physical",
            {type: google.maps.MapTypeId.TERRAIN}
        );
        var gmap = new OpenLayers.Layer.Google(
            "Google Streets", // the default
            {numZoomLevels: 20}
        );
        var ghyb = new OpenLayers.Layer.Google(
            "Google Hybrid",
            {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20}
        );
        var gsat = new OpenLayers.Layer.Google(
            "Google Satellite",
            {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 20}
        );

        map.addLayers([gphy,gmap,ghyb,gsat]);

        var latLongProj = new OpenLayers.Projection("EPSG:4326");

        $.ajax("${createLink(controller: 'ajax', action: 'getPlots')}").done(function(data) {

          var plots = new OpenLayers.Layer.Markers("Plots");
          var results = data.results;
          for (resultKey in results) {

            var result = results[resultKey];
            var location = new OpenLayers.LonLat(parseFloat(result.longitude),parseFloat(result.latitude));

            location.transform(latLongProj, map.getProjectionObject());

            var marker = new OpenLayers.Marker(location);
            plots.addMarker(marker);
            marker.tag = result;

            marker.events.register('mouseover', marker, function(evt) {
              showPlotSummary(this.tag);
            });

            marker.events.register('mouseout', marker, function(evt) {
              hidePlotSummary(this.tag);
            });

            marker.events.register('click', marker, function(evt) {
              showPlotDetails(this.tag);
            });

          }
          plots.id ="Plots";

          map.addLayer(plots);
          map.zoomToExtent(plots.getDataExtent());
          // map.setLayerIndex(plots, 999); //set the marker layer to an arbitrarily high layer index

          hideMessagePanel();
        });


        var point = new OpenLayers.LonLat(133, -28);
        point.transform(latLongProj, map.getProjectionObject());
        map.setCenter(point, 5);
      }

      function showPlotDetails(plot) {
        $("#plotDetailsContent").attr("plotName", plot.siteName);
        $("#plotDetailsLink").click();
        return true;
      }

      function showPlotSummary(plot) {
        var html = "<h4>Plot " + plot.siteName + "</h4>"
        html += "<table>"
        html += "<tr><td>Date:</td><td>" + plot.date + "</td></tr>";
        html += "<tr><td>Longitude:</td><td>" + plot.longitude + "</td></tr>";
        html += "<tr><td>Latitude:</td><td>" + plot.latitude + "</td></tr>";
        html += "</table>"

        $("#plotSummary").css("display", "block").html(html);
      }

      function hidePlotSummary(plot) {
        hideMessagePanel();
      }

      function hideMessagePanel() {
        $('#plotSummary').css("display", "none");
      }

      function infoMessage(message) {
        var html = "<h4>" + message + "</h4>"
        $("#plotSummary").css("display", "block").html(html);
      }

    </script>

    <div class="container-fluid">
      <div class="row-fluid">
        <div class="span9">
          <div id="mapContent">
          </div>
        </div>
        <div class="span3">

          <h4>Selected plots</h4>
          <div style="height: 100px;">
            <table class="table table-bordered table-condensed">
              <tr>
                <td>Plot 1 <button class="btn btn-mini pull-right"><i class="icon-trash"/></button></td>
              </tr>
              <tr>
                <td>Plot 2 <button class="btn btn-mini pull-right"><i class="icon-trash"/></button></td>
              </tr>
            </table>
          </div>

          <div class="" style="margin-top: 5px">
            <button class="btn btn-small btn-primary">Compare selected</button>
            <button class="btn btn-small">Clear selection</button>
          </div>

          <h4>Environmental layers</h4>
          <div style="height: 150px; overflow-y: scroll;">

            <table class="table table-striped table-condensed">
              <tr>
                <td>Layer name 1</td><td><button class="btn btn-mini pull-right"><i class="icon-eye-open" /></button></td><td><button class="btn btn-mini pull-right"><i class="icon-trash"/></button></td>
              </tr>
              <tr>
                <td>Layer name 2</td><td><button class="btn btn-mini pull-right"><i class="icon-eye-open" /></button></td><td><button class="btn btn-mini pull-right"><i class="icon-trash"/></button></td>
              </tr>
              <tr>
                <td>Layer name 3</td><td><button class="btn btn-mini pull-right"><i class="icon-eye-open" /></button></td><td><button class="btn btn-mini pull-right"><i class="icon-trash"/></button></td>
              </tr>
              <tr>
                <td>Layer name 4</td><td><button class="btn btn-mini pull-right"><i class="icon-eye-open" /></button></td><td> <button class="btn btn-mini pull-right"><i class="icon-trash"/></button></td>
              </tr>
              <tr>
                <td>Layer name 5</td><td><button class="btn btn-mini pull-right"><i class="icon-eye-open" /></button></td><td> <button class="btn btn-mini pull-right"><i class="icon-trash"/></button></td>
              </tr>
              <tr>
                <td>Layer name 6</td><td><button class="btn btn-mini pull-right"><i class="icon-eye-open" /></button></td><td> <button class="btn btn-mini pull-right"><i class="icon-trash"/></button></td>
              </tr>
              <tr>
                <td>Layer name 7</td><td><button class="btn btn-mini pull-right"><i class="icon-eye-open" /></button></td><td> <button class="btn btn-mini pull-right"><i class="icon-trash"/></button></td>
              </tr>
              <tr>
                <td>Layer name 8</td><td> <button class="btn btn-mini pull-right"><i class="icon-trash"/></button></td>
              </tr>
            </table>
          </div>

          <div class="" style="margin-top: 5px">
            <button id="btnLayerAdd" class="btn btn-small btn-success">Add Layer <i class="icon-plus icon-white"></button>
          </div>
        </div>
      </div>
    </div>

    <div class="info-popup" id="plotSummary" style="display: none; ">
      <H2>Plot details</H2>
    </div>

    <a id="addLayerLink" href="#addLayerDetails" style="display: none"></a>

    <div id="addLayerDetails" style="display:none; width: 600px; height: 300px">
      <div id="addLayerContent">
      </div>
    </div>

    <a id="plotDetailsLink" href="#plotDetails" style="display: none"></a>

    <div id="plotDetails" style="display:none; width: 600px; height: 300px">
      <div id="plotDetailsContent">
      </div>
    </div>

    <content tag="buttonBar">
      <button class="btn" id="btnFindPlot">Find Plots <i class="icon-search"></i></button>
      %{--<button class="btn btn-success" id="btnLayerAdd">Add layer <i class="icon-plus icon-white"></i></button>--}%
    </content>
	</body>
</html>
