<div>
  <h5>${layerInfo.displayname}</h5>
  <table class="table table-condensed">
    <tr>
      <td>
        <img src="http://spatial.ala.org.au/geoserver/wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH=20&HEIGHT=5&LAYER=${layerName}" alt="Layer legend" />
      </td>
      <td style="text-align: center">
          <div style="margin-bottom: 10px"><small>Opacity</small></div>
          <div id="opacitySlider" style="margin-left: auto; margin-right: auto"></div>
          <div style="margin-top: 10px" id="opacityValue"><small>${Math.round((layerInstance.opacity ?: 1.0) * 100)}%</small></div>
      </td>
    </tr>
  </table>
</div>

<script type="text/javascript">
  var startingOpacity = ${layerInstance.opacity ?: 1.0} * 100;
  $("#opacitySlider").slider({
    min: 0,
    max: 100,
    value: startingOpacity,
    orientation: "vertical",
    change: function(event, ui) {
      $("#opacityValue").html(ui.value + "%");
      setLayerOpacity('${layerName}', ui.value / 100);
    },
    slide: function(event, ui) {
      $("#opacityValue").html("<small>" + ui.value + "%</small>");
    }

  });

</script>