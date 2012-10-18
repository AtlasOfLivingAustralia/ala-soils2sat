<div>
  <div id="layerTree" style="max-height: 250px; height: 240px; overflow-y: scroll;">
    <ul item-selected="true">
      <g:each in="${layerMap}" var="topLevel">
        <g:set value="${topLevel.key}" var="topLevelLabel"/>
        <g:if test="${topLevel.key == '_'}">
          <g:set var="topLevelLabel" value="Unclassified"/>
        </g:if>
        <g:if test="${topLevel.value}">
          <li><b>${topLevelLabel}</b>
            <ul>
              <g:each in="${topLevel.value}" var="secondLevel">
                <g:if test="${secondLevel.key != '_'}">
                  <li><b>${secondLevel.key}</b>
                    <ul>
                      <g:each in="${secondLevel.value}" var="layer">
                        <sts:layerTreeItem layer="${layer}" />
                      </g:each>
                    </ul>
                  </li>
                </g:if>
              </g:each>
              <g:each in="${topLevel.value['_']}" var="unsorted">
                <sts:layerTreeItem layer="${unsorted}" />
              </g:each>
            </ul>
          </li>
        </g:if>
      </g:each>
    </ul>
</div>

<div class="well well-small" style="margin-top: 10px; margin-bottom: 0px; height:130px;">
  <div id="browseLayerInfo">
  </div>
</div>


<script type="text/javascript">
  var theme = "ui-smoothness";
//  $("#browseExpander").jqxExpander({ showArrow: false, toggleMode: 'none', width: '300px', height: 'auto', theme: theme });

  $("#layerTree").jqxTree({
    theme: theme
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