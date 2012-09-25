<div>
  <h3>Add an environmental layer</h3>
  <div style="vertical-align: top">
    <input id="layer" placeholder="Search..." class="ui-autocomplete-input" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" style="width:400px; margin-bottom: 10px">
    <span style="vertical-align: top">Add layer to map&nbsp;<g:checkBox style="vertical-align: top" id="chkAddToMap" name="chkAddToMap" checked=""/></span>
  </div>
  <button id="btnLoadLayer" class="btn btn-small btn-primary">Load layer</button>
  <button id="btnCancelLoadLayer" class="btn btn-small">Close</button>
  <div id="layerInfoPanel" class="well well-large" style="margin-top:10px">

  </div>
</div>

<script type="text/javascript">

  $('#btnCancelLoadLayer').click(function(e) {
    $.fancybox.close();
  });

  function loadSelectedLayer() {
    var layerName = $("#selectedLayer").attr("layerName");
    if (layerName) {
      var addToMap = $('#chkAddToMap').attr('checked');
      addLayer(layerName, addToMap);
    }
  }

  $('#btnLoadLayer').click(function(e) {
    loadSelectedLayer();
  });

  $("#layer").autocomplete({
      source : function(request, response) {
          $("#layerInfoPanel").html("");
          $.ajax({
              url : "${createLink(controller: 'spatialProxy', action:'layersSearch')}",
              dataType : "json",
              data: {
                  q: request.term
              },
              success : function(data) {
                  response($.map(data, function(item) {
                      return {
                          label : item.displayname,
                          value : item.displayname,
                          id : item.uid,
                          name : item.name,
                          description : item.description,
                          licence : item.licence_notes,
                          classification1: item.classification1,
                          classification2: item.classification2
                      }
                  }));
              },
              error : function(jqXHR, textStatus, errorThrown) {
                  alert("Unable to complete request.\n" + errorThrown);
              }
          });
      },
      minLength : 3,
      html : true,
      select : function(event, ui) {
        var item = ui.item;
        var html= '<div id="selectedLayer" layerName="' + item.name + '"><strong>' + item.label + " (" + item.name + ")</strong><br/>" + item.description + '<br /><small><span class="label">' + item.classification1 + '</span>'
        if (item.classification2) {
            html += '&nbsp;<span class="label">' + item.classification2 + '</span>';
        }
        html += '</small></div>';

        $("#layerInfoPanel").html(html);
      }
  });

//  $("#layer").keydown(function(e) {
//    if (e.keyCode == 13) {
//      loadSelectedLayer();
//    }
//  });

  $("#layer").focus();

</script>