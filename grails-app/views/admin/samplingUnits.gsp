<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Sampling Units</title>
    </head>

    <body>
        <content tag="pageTitle">Sampling Units</content>

        <content tag="adminButtonBar">
        </content>

        <div class="">
            <h5>Sampling Units</h5>
            <div class="control-group">
                <div class="controls">
                    <g:textField id="samplingUnit" style="margin-bottom: 0" name="unit" class="input-xlarge"/>
                    <button id="btnAddUnit" class="btn btn-primary"><i class="icon-plus icon-white"></i>&nbsp;Add</button>
                </div>
            </div>
            <div id="samplingUnits">
                <g:if test="${samplingUnits}">
                    <table class="table table-condensed table-striped table-bordered">
                        <g:each in="${samplingUnits}" var="unit">
                            <tr samplingUnitId="${unit.id}">
                                <td>${unit.name}</td>
                                <td><button class="btn btn-mini btn-danger btnDeleteUnit"><i class="icon-trash icon-white"></i>&nbsp;delete</button></td>
                            </tr>
                        </g:each>
                    </table>
                </g:if>
                <g:else>
                    <div class="alert alert-info">
                        No Sampling Units have been defined yet
                    </div>
                </g:else>
            </div>
        </div>

        <script type="text/javascript">
            $(document).ready(function() {

                $("#btnAddUnit").click(function(e) {
                    e.preventDefault();
                    var name = $("#samplingUnit").val();
                    if (name) {
                        window.location = "${createLink(controller:'admin', action:'addSamplingUnit')}?name=" + name;
                    }
                });

                $(".btnDeleteUnit").click(function(e) {
                    e.preventDefault();
                    var unitId = $(this).parents("[samplingUnitId]").attr("samplingUnitId");
                    if (unitId) {
                        window.location = "${createLink(controller:'admin', action:'deleteSamplingUnit')}?samplingUnitId=" + unitId;
                    }
                });

            });
        </script>

    </body>
</html>
