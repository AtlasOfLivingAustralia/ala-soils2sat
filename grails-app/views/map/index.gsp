<!doctype html>
<html>
	<head>
    <r:require module='jqueryui' />
    <r:require module='openlayers' />

		<meta name="layout" content="soils2sat"/>
		<title>Soils to Satellites</title>
		<style type="text/css" media="screen">
      #content {
        top: 63px;
        bottom: 90px;
        position: absolute;
        width: 100%;
      }

      #mapContent {
        width: 100%;
        height: 100%;
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

		</style>

    <link rel="stylesheet" href="${resource(dir: 'js/openlayers/theme/default', file: 'style.css')}" type="text/css">

    <script src='http://maps.google.com/maps?file=api&amp;v=2&amp;key=AIzaSyDAqE0sOMHNienA0zFIUMlY53ztEDiv0d8'></script>
	</head>
	<body>
    <script type="text/javascript">
      $(document).ready( function (e) {
        initMap();
      });

      var map;

      function initMap() {

          map = new OpenLayers.Map('mapContent', {controls: [
            new OpenLayers.Control.Navigation({ mouseWheelOptions: {interval: 50, cumulative: false}, zoomBoxEnabled: true } ),
            new OpenLayers.Control.ArgParser(),
            new OpenLayers.Control.PanZoom(),
            new OpenLayers.Control.PanZoomBar()

          ]});
          map.addControl(new OpenLayers.Control.LayerSwitcher());

          var gphy = new OpenLayers.Layer.Google(
              "Google Physical",
              {type: G_PHYSICAL_MAP}
          );
          var gmap = new OpenLayers.Layer.Google(
              "Google Streets", // the default
              {numZoomLevels: 20}
          );
          var ghyb = new OpenLayers.Layer.Google(
              "Google Hybrid",
              {type: G_HYBRID_MAP, numZoomLevels: 20}
          );
          var gsat = new OpenLayers.Layer.Google(
              "Google Satellite",
              {type: G_SATELLITE_MAP, numZoomLevels: 22}
          );

          var format = 'image/png';
          tiled = new OpenLayers.Layer.WMS(
              "ALA:elevation - Tiled", "http://spatial.ala.org.au/geoserver/ALA/wms",
              {
                  LAYERS: 'ALA:elevation',
                  STYLES: '',
                  format: format,
                  tiled: true,
                  transparent: true,
                  SRS: 'EPSG:900913',
                  tilesOrigin : map.maxExtent.left + ',' + map.maxExtent.bottom
              },
              {
                  buffer: 0,
                  displayOutsideMaxExtent: true,
                  isBaseLayer: false
//                ,
//                  yx : {'EPSG:900913' : true}
              }
          );

          map.addLayers([tiled]);

          var plots = new OpenLayers.Layer.Markers("Plots");

          $.ajax("${createLink(controller: 'ajax', action: 'getPlots')}").done(function(data) {
            var results = data.results;
            for (resultKey in results) {
              var result = results[resultKey];
//              var size = new OpenLayers.Size(21,25);
//              var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
//              var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png', size, offset);

              var location = new OpenLayers.LonLat(parseFloat(result.longitude),parseFloat(result.latitude));
              var marker = new OpenLayers.Marker(location);
              plots.addMarker(marker);
              marker.tag = result;

              marker.events.register('mouseover', marker, function(evt) {
                showPlotDetails(this.tag);
              });

              marker.events.register('mouseout', marker, function(evt) {
                hidePlotDetails(this.tag);
              });


            }

            map.zoomToExtent(plots.getDataExtent());

          });


          map.addLayers([gphy, gmap, ghyb, gsat, plots]);

          map.setCenter(new OpenLayers.LonLat(149, -27), 5);
      }

      function showPlotDetails(plot) {
        var html = "<h4>Plot " + plot.siteName + "</h4>"
        html += "<table>"
        html += "<tr><td>Date:</td><td>" + plot.date + "</td></tr>";
        html += "<tr><td>Longitude:</td><td>" + plot.longitude + "</td></tr>";
        html += "<tr><td>Latitude:</td><td>" + plot.latitude + "</td></tr>";
        html += "</table>"

        $("#plotSummary").css("display", "block").html(html);
      }

      function hidePlotDetails(plot) {
        $('#plotSummary').css("display", "none");
      }

    </script>
    <div id="content">
      <div id="mapContent">
      </div>
    </div>
    <div class="info-popup" id="plotSummary" style="display: none; ">
      <H2>Plot details</H2>
    </div>
	</body>
</html>
