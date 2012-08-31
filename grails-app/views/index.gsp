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
          // map.addControl();

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


          map.addLayers([gphy, gmap, ghyb, gsat]);

          map.setCenter(new OpenLayers.LonLat(149, -27), 5);
      }

    </script>
    <div id="content">
      <div id="mapContent">
      </div>
    </div>
	</body>
</html>
