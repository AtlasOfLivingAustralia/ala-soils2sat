<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
	<head>
    <r:require module='jqueryui' />
    <r:require module='bootstrap_responsive' />
		<meta name="layout" content="admin"/>
		<title>Soils to Satellites - Admin - Layer sets</title>
	</head>
	<body>
    <content tag="pageTitle">Layer Sets</content>
    <table class="table table-bordered table-striped">
      <thead>
        <tr>
          <th>Set Name</th>
          <th>Layer count</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${layerSets}" var="layerSet">
          <tr layerSetId="${layerSet.id}">
            <td>${StringEscapeUtils.escapeHtml(layerSet.name)}</td>
            <td>${layerSet.layers?.size()}</td>
            <td>
              <button class="btn btn-mini btn-danger btnDeleteLayerSet"><i class="icon-trash icon-white"></i>&nbsp;delete</button>
              <button class="btn btn-mini btnEditLayerSet"><i class="icon-edit"></i>&nbsp;edit</button>
            </td>
          </tr>
        </g:each>

      </tbody>
    </table>
    <content tag="adminButtonBar">
      <button class="btn btn-small btn-primary" id="btnAddLayerSet"><i class="icon-plus icon-white"></i>&nbsp;Add Layer Set</button>
    </content>
    <script type="text/javascript">

      $("#btnAddLayerSet").click(function() {
        window.location = "${createLink(controller:'admin', action:'newGlobalLayerSet')}"
      });

      $(".btnDeleteLayerSet").click(function(e) {
        var layerSetId = $(this).parents("tr[layerSetId]").attr("layerSetId");
        if (layerSetId) {
          if (confirm("Are you sure you wish to delete global layer set " + layerSetId + "?")) {
            window.location = "${createLink(controller:'admin', action:'deleteLayerSet')}/" + layerSetId;
          }
        }
      });

      $(".btnEditLayerSet").click(function(e) {
        var layerSetId = $(this).parents("tr[layerSetId]").attr("layerSetId");
        if (layerSetId) {
          window.location = "${createLink(controller:'admin', action:'editLayerSet')}/" + layerSetId;
        }
      });

    </script>
  </body>
</html>
