<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Ecological Contexts</title>
    </head>

    <body>
        <content tag="pageTitle">Ecological Contexts</content>

        <content tag="adminButtonBar">
            <button id="btnDeleteAll" class="btn btn-danger btn-small"><i class="icon-remove icon-white"></i>&nbsp;Delete all contexts</button>
            <button id="btnAutoGenerateContexts" class="btn btn-small"><i class="icon-wrench"></i>&nbsp;Auto generate contexts</button>
            <button id="btnAddContext" class="btn btn-small btn-primary"><i class="icon-plus icon-white"></i>&nbsp;Add context</button>
        </content>

        <table class="table table-bordered table-striped table-condensed">
            <thead>
                <tr>
                    <th>Ecological Context</th>
                    <th>Sampling Units</th>
                    <th style="width: 120px">Actions</th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${contexts}" var="context">
                    <tr ecologicalContextId="${context.id}">
                        <td style="width: 300px">${context.name}</td>
                        <td>${context.samplingUnits.collect({ it.name }).join(", ")}</td>
                        <td>
                            <button class="btn btn-mini btn-danger btnDeleteContext"><i class="icon-remove icon-white"></i>&nbsp;delete</button>
                            <button class="btn btn-mini btnEditContext"><i class="icon-edit"></i>&nbsp;edit</button>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>


        <script type="text/javascript">

            $(document).ready(function() {

                $("#btnAutoGenerateContexts").click(function(e) {
                    window.location = "${createLink(controller:'admin', action:'generateEcologicalContexts')}";
                });

                $("#btnAddContext").click(function(e) {
                    window.location = "${createLink(controller: 'admin', action:'newEcologicalContext')}";
                });

                $(".btnDeleteContext").click(function (e) {
                    e.preventDefault();
                    var contextId = $(this).parents("[ecologicalContextId]").attr("ecologicalContextId");
                    if (contextId) {
                        if (confirm("Are you sure you wish to delete this ecological context?")) {
                            window.location = "${createLink(controller:'admin', action: 'deleteEcologicalContext')}?ecologicalContextId=" + contextId;
                        }
                    }
                });

                $(".btnEditContext").click(function (e) {
                    e.preventDefault();
                    var contextId = $(this).parents("[ecologicalContextId]").attr("ecologicalContextId");
                    if (contextId) {
                        window.location = "${createLink(controller:'admin', action: 'editEcologicalContext')}?ecologicalContextId=" + contextId;
                    }
                });

                $("#btnDeleteAll").click(function(e) {
                    e.preventDefault();
                    if (confirm("Are you sure you wish to delete all Ecological Contexts?")) {
                        window.location = "${createLink(action:"deleteAllEcologicalContexts")}";
                    }
                });

            });



        </script>

    </body>
</html>
