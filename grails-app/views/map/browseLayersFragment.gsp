<div id="layerTree" style="max-height: 250px; height: 240px; overflow-y: scroll;">
    <ul item-selected="true">
      <g:each in="${layerTree.childFolders}" var="folder">
        <li><b>${folder.label}</b>
          <ul>
            <g:each in="${folder.childFolders}" var="subfolder">
              <li><b>${subfolder.label}</b>
                <ul>
                  <g:each in="${subfolder.layers}" var="layer">
                    <sts:layerTreeItem layer="${layer}" />
                  </g:each>
                </ul>
              </li>
            </g:each>

            <g:each in="${folder.layers}" var="layer">
              <sts:layerTreeItem layer="${layer}" />
            </g:each>
          </ul>
        </li>
      </g:each>
      <g:if test="${layerTree.layers}">
        <li><b>Unclassified</b>
          <ul>
            <g:each in="${layerTree.layers}" var="layer">
              <sts:layerTreeItem layer="${layer}" />
            </g:each>
          </ul>
        </li>
      </g:if>
    </ul>
</div>

<div class="well well-small" style="margin-top: 10px; margin-bottom: 0px; height:140px;">
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