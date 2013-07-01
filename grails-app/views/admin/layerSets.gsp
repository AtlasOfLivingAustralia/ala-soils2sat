<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Layer sets</title>
    </head>

    <body>
        <content tag="pageTitle">Layer Sets</content>

        <content tag="adminButtonBar">
            <button id="btnImport" class="btn btn-small"><i class="icon-upload"></i>&nbsp;Import layer sets</button>
            <button id="btnExport" class="btn btn-small"><i class="icon-download-alt"></i>&nbsp;Export layer sets</button>
            <button class="btn btn-small btn-primary" id="btnAddLayerSet"><i class="icon-plus icon-white"></i>&nbsp;Add Layer Set</button>
        </content>

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
                            <button class="btn btn-mini btn-danger btnDeleteLayerSet"><i class="icon-remove icon-white"></i>&nbsp;delete</button>
                            <button class="btn btn-mini btnEditLayerSet"><i class="icon-edit"></i>&nbsp;edit</button>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>
        <script type="text/javascript">

            $(document).ready(function() {

                $("#btnAddLayerSet").click(function () {
                    window.location = "${createLink(controller:'admin', action:'newGlobalLayerSet')}"
                });

                $(".btnDeleteLayerSet").click(function (e) {
                    var layerSetId = $(this).parents("tr[layerSetId]").attr("layerSetId");
                    if (layerSetId) {
                        if (confirm("Are you sure you wish to delete global layer set " + layerSetId + "?")) {
                            window.location = "${createLink(controller:'admin', action:'deleteLayerSet')}/" + layerSetId;
                        }
                    }
                });

                $(".btnEditLayerSet").click(function (e) {
                    var layerSetId = $(this).parents("tr[layerSetId]").attr("layerSetId");
                    if (layerSetId) {
                        window.location = "${createLink(controller:'admin', action:'editLayerSet')}/" + layerSetId;
                    }
                });

                $("#btnExport").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(action:'exportLayerSets')}";
                });

                $("#btnImport").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(action:'selectImportFile', params:[importType:'layerSets'])}";
                });

            });

        </script>
    </body>
</html>
