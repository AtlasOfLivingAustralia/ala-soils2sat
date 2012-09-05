<div>
  <h3>Add an environmental layer</h3>
  <input id="layer" placeholder="Search..." class="ui-autocomplete-input" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" style="width:400px; margin-bottom: 10px">
  <br />
  <button id="btnLoadLayer" class="btn btn-primary">Load layer</button>
  <button id="btnCancelLoadLayer" class="btn">Cancel</button>

  <div id="layerInfo" class="well well-large" style="margin-top:10px">

  </div>
</div>

<script type="text/javascript">

  $('#btnCancelLoadLayer').click(function(e) {
    $.fancybox.close();
  });

  $('#btnLoadLayer').click(function(e) {
    var layerName = $("#selectedLayer").attr("layerName");
    if (layerName) {
      loadWMSLayer(layerName);
      $.fancybox.close();
    }
  });

  $("#layer").autocomplete({
      source : function(request, response) {
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
        var html= '<div id="selectedLayer" layerName="' + item.name + '"><strong>' + item.label + " (" + item.name + ")</strong><br/>" + item.description + '<br /><small><span class="label">' + item.classification1 + '</span>&nbsp;<span class="label">' + item.classification2 + "</span></small></div>";
        $('#layerInfo').html(html);
      }
  });

</script>