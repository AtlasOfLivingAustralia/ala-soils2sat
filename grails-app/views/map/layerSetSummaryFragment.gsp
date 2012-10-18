<div style="padding-bottom: 5px;">
  <table style="width: 100%">
    <tr>
      <td><h4 style="margin-top: 0px">${layerSet.name} <small>${layerSet.description}</small></h4></td>
      <td style="text-align: right; width: 350px">
        <span>
          <g:checkBox style="margin-top: 0px" name="chkClearExisting" id="chkClearExisting"/>&nbsp;Replace existing layers
          <button id="btnAddLayerSet" class="btn btn-primary btn-small"><i class="icon-plus icon-white"></i>&nbsp;Add Layers</button>
          <sts:ifAdmin>
            <button class="btn btn-small btn-warning" id="btnEditLayerSet">Edit</button>
          </sts:ifAdmin>

        </span>
      </td>
    </tr>
  </table>
</div>

<div style="max-height: 170px; height:170px; overflow-y: scroll;">
  <table class="table table-condensed table-striped table-bordered">
    <tbody>
      <g:each in="${layerSet.layers}" var="layerName">
        <tr>
          <td>${layerName}</td>
          <td>${layerDescriptions[layerName]}</td>
        </tr>
      </g:each>
    </tbody>
  </table>
</div>
<script type="text/javascript">

  $("#btnAddLayerSet").click(function(e) {
    e.preventDefault();
    var replaceExisting = $("#chkClearExisting").prop('checked');
    addLayerSet(${layerSet.id}, replaceExisting);
    $.fancybox.close();
  });

  $("#btnEditLayerSet").click(function (e) {
    e.preventDefault();
    window.location = "${createLink(controller: 'admin', action:'editLayerSet', id:layerSet.id)}";
  });

</script>