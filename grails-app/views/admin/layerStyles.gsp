<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Layer Styles</title>
    </head>

    <body>
        <content tag="pageTitle">Layer Styles</content>

        <content tag="adminButtonBar">
            <button class="btn btn-small btn-primary" id="btnAddLayerStyle"><i class="icon-plus icon-white"></i>&nbsp;Add Layer Style Mapping</button>
        </content>

        <div class="well well-small">
            Maps Geo-Server style names to layers (styles need to be already defined on the ALA Spatial Portal)
        </div>

        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th>Layer Name</th>
                    <th>Style(s)</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${layerStyles}" var="layerStyle">
                    <tr layerStyleId="${layerStyle.id}">
                        <td>${StringEscapeUtils.escapeHtml(layerStyle.layerName)}</td>
                        <td>${StringEscapeUtils.escapeHtml(layerStyle.style)}</td>
                        <td>
                            <button class="btn btn-mini btn-danger btnDeleteLayerStyle"><i class="icon-remove icon-white"></i>&nbsp;delete</button>
                            <button class="btn btn-mini btnEditLayerStyle"><i class="icon-edit"></i>&nbsp;edit</button>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>
        <script type="text/javascript">

            $(document).ready(function() {

                $("#btnAddLayerStyle").click(function () {
                    showModal({
                        url: "${createLink(controller: 'admin', action:'editLayerStyleFragment')}",
                        title: "Add Layer Style mapping",
                        height: 250,
                        width: 500
                    });
                });

                $(".btnDeleteLayerStyle").click(function (e) {
                    e.preventDefault();
                    var layerStyleId = $(this).parents("tr[layerStyleId]").attr("layerStyleId");
                    if (layerStyleId) {
                        if (confirm("Are you sure you wish to delete the style mapping " + layerStyleId + " ?")) {
                            window.location = "${createLink(controller:'admin', action:'deleteLayerStyle')}/" + layerStyleId;
                        }
                    }
                });

                $(".btnEditLayerStyle").click(function (e) {
                    e.preventDefault();
                    var layerStyleId = $(this).parents("tr[layerStyleId]").attr("layerStyleId");
                    if (layerStyleId) {
                        showModal({
                            url: "${createLink(controller: 'admin', action:'editLayerStyleFragment')}/" + layerStyleId,
                            title: "Add Layer Style mapping",
                            height: 250,
                            width: 500
                        });
                    }
                });

            });

        </script>
    </body>
</html>
