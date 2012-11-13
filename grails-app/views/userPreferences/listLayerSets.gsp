<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
	<head>
        <r:require module='jqueryui' />
        <r:require module='bootstrap_responsive' />
		<meta name="layout" content="detail"/>
		<title>Your layer sets</title>
	</head>
	<body>
        <div class="container">
            <legend>
              <table style="width:100%">
                <tr>
                  <td>User Defined Layer Sets</td>
                  <td><button id="btnAddLayerSet" class="btn btn-small btn-primary pull-right"><i class="icon-plus icon-white"></i>&nbsp;Add Layer Set</button></td>
                </tr>
              </table>
            </legend>

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
                                <button class="btn btn-mini btn-danger btnDeleteLayerSet"><i class="icon-trash icon-white"></i>&nbsp;delete</button>&nbsp;
                                <button class="btn btn-mini btnEditLayerSet"><i class="icon-edit"></i>&nbsp;edit</button>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </div>
        <script type="text/javascript">

            $("#btnAddLayerSet").click(function(e) {
                e.preventDefault();
                window.location = "${createLink(controller:'userPreferences', action:'newLayerSet')}";
            });

            $(".btnDeleteLayerSet").click(function(e) {
                e.preventDefault();
                var layerSetId = $(this).parents("tr[layerSetId]").attr("layerSetId");
                if (layerSetId) {
                  if (confirm("Are you sure you wish to delete this layer set?")) {
                    window.location = "${createLink(controller:'userPreferences', action:'deleteLayerSet')}?layerSetId=" + layerSetId;
                  }
                }
            });

            $(".btnEditLayerSet").click(function(e) {
                e.preventDefault();
                var layerSetId = $(this).parents("tr[layerSetId]").attr("layerSetId");
                if (layerSetId) {
                    window.location = "${createLink(controller:'userPreferences', action:'editLayerSet')}?layerSetId=" + layerSetId;
                }
            });


        </script>
    </body>
</html>