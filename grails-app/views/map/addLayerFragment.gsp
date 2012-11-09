<style type="text/css">

  .tab-content {
    border-left: 1px solid #d3d3d3;
    border-right: 1px solid #d3d3d3;
    border-bottom: 1px solid #d3d3d3;
    padding: 10px;
    background-color: white;
  }

</style>
<div>
  %{--<h4 style="margin-top: 0px">Add an environmental layer</h4>--}%
  <div class="tabbable">

    <ul class="nav nav-tabs" style="margin-bottom: 0px">
      <li class="active"><a href="#searchTab" data-toggle="tab">Search</a></li>
      <li><a href="#browseTab" data-toggle="tab">Browse</a></li>
      <li><a href="#layerSetsTab" data-toggle="tab">Layer sets</a></li>
    </ul>

    <div class="tab-content" >

      <div class="tab-pane active" id="searchTab" style="vertical-align: top; margin-bottom: 5px;margin-top:30px;">
        <input id="layer" placeholder="Search..." class="ui-autocomplete-input" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" style="width:400px; margin-bottom: 10px">
        %{--<span style="vertical-align: top">Add layer to map&nbsp;<g:checkBox style="vertical-align: top" id="chkAddToMap" name="chkAddToMap" checked=""/></span>--}%
        <br />
        <div id="layerInfoPanel" class="well well-small" style="margin-top:10px; height: 130px">
        </div>

      </div>

      <div class="tab-pane" id="browseTab">
      </div>

      <div class="tab-pane" id="layerSetsTab">
      </div>

    </div>

  </div>

</div>

<script type="text/javascript">

  $('#btnCancelLoadLayer').click(function(e) {
    $.fancybox.close();
  });

  $('a[data-toggle="tab"]').on('shown', function (e) {

    $("#browseTab").html("");
    $("#layerInfoPanel").html("");
    $("#layer").val("");

    var tabHref = $(this).attr('href');
    if (tabHref == '#browseTab') {
      $("#browseTab").html('Retrieving layer information... <img src="${resource(dir:'/images', file:'spinner.gif')}"/></div>');
      $.ajax("${createLink(controller: 'map', action: 'browseLayersFragment')}").done(function(html) {
        $("#browseTab").html(html);
      });
    } else if (tabHref == "#layerSetsTab") {
      $("#layerSetsTab").html("Retrieving layer sets...");
      $.ajax("${createLink(controller: 'map', action: 'layerSetsFragment')}").done(function(html) {
        $("#layerSetsTab").html(html);
      });

    }
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
        $("#layerInfoPanel").html("");
        $.ajax("${createLink(controller: 'map', action:'layerSummaryFragment')}?layerName=" + item.name).done(function(content) {
          $("#layerInfoPanel").html(content);
        });
      }
  });

  $("#layer").focus();

</script>