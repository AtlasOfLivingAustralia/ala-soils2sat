<div>
  <div id="layerSetTree" style="max-height: 160px; height: 160px; overflow-y: scroll;">
    <ul item-selected="true">
      <li><b>Predefined Layer Sets</b>
        <ul>
          <g:each in="${globalLayerSets}" var="layerSet">
            <li layerSetId="${layerSet.id}">${layerSet.name} (${layerSet.layers.size()} layers)
            </li>
          </g:each>
        </ul>
      </li>
      <li><b>User Defined Layer Sets</b>&nbsp;<button class="btn btn-mini">Edit layer sets</button>
        <ul>
          <li>Coming soon...</li>
        </ul>
      </li>
    </ul>
  </div>
</div>

<div class="well well-small" style="margin-top: 10px; margin-bottom: 0px; height:210px;">
  <div id="layerSetInfo">
  </div>
</div>



<script type="text/javascript">

  var theme = "ui-smoothness";

  $("#layerSetTree").jqxTree({
    theme: theme
  }).bind('select', onLayerSetSelect);

  function onLayerSetSelect(event) {
    var layerSetId = $(event.args.element).attr("layerSetId");
    if (layerSetId) {
      $("#layerSetInfo").html('Retrieving layer set information... <img src="${resource(dir:'/images', file:'spinner.gif')}"/></div>');
      $.ajax("${createLink(controller:'map', action:'layerSetSummaryFragment')}?layerSetId=" + layerSetId).done(function(content) {
        $("#layerSetInfo").html(content);
      });
    } else {
      $("#layerSetInfo").html("");
    }
  }

</script>