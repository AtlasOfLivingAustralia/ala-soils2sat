<div style="max-height: 250px; height: 250px; overflow-y: scroll;">
  <div id="layerTree">
    <ul item-selected="true">
    <g:each in="${layerMap}" var="kvp">
      <li><b>${kvp.key}</b>
        <ul>
          <g:each in="${layerMap[kvp.key]}" var="layer">
            <li layerName="${layer.name}">${layer.displayname} <small>- ${layer.description}</small></li>
          </g:each>
        </ul>
      </li>
    </g:each>
    </ul>
  </div>
</div>

<div class="well well-small" style="margin-top: 10px; margin-bottom: 0px; height:130px;">
  <div id="browseLayerInfo">
  </div>
</div>


<script type="text/javascript">

  $("#layerTree").jqxTree({
    theme: 'ui-smoothness'
  }).bind('select', onLayerSelect);

  function onLayerSelect(event) {
    var layerName = $(event.args.element).attr("layerName");
    if (layerName) {
      $("#browseLayerInfo").html("Retrieving layer information...");
      $.ajax("${createLink(controller: 'map', action:'layerSummaryFragment')}?layerName=" + layerName).done(function(content) {
        $("#browseLayerInfo").html(content);
      });
    } else {
      $("#browseLayerInfo").html("");
    }
  }

</script>