<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <meta name="layout" content="detail"/>
        <title>Species Analysis</title>
    </head>

    <body>

        <style type="text/css">
        </style>

        <script type="text/javascript">

            $(document).ready(function () {

                $(".layerInfoLink").click(function(e) {
                    e.preventDefault();
                    var layerName = $(this).parents("[layerName]").attr("layerName");
                    if (layerName && displayLayerInfo) {
                        displayLayerInfo(layerName);
                    }
                });
            });

        </script>

        <div class="container-fluid">
            <legend>
                <table style="width:100%">
                    <tr>
                        <td>
                            <sts:homeBreadCrumb />
                            <sts:navSeperator/>
                            <span class="sts-breadcrumb">${flowTitle}</span>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </legend>

            <div class="row-fluid">
                <div class="span12">
                    <g:form>
                        <div class="well well-small">
                            <h4>Environmental Layers</h4>
                            <table>
                                <g:each in="${appState.layers}" var="layer">
                                    <tr layerName="${layer.name}">
                                        <g:set var="isChecked" value="${selectedLayers?.contains(layer.name)}" />
                                        <td style="width:30px; vertical-align: middle"><g:checkBox style="display: inline-block; vertical-align: middle; margin-top:0" name="layerName" checked="${isChecked}" value="${layer.name}" /></td>
                                        <td><a href="#" class="layerInfoLink"><sts:renderEnvironmentLayerName layerName="${layer.name}" /></a></td>
                                    </tr>
                                </g:each>
                            </table>
                        </div>
                        <g:link class="btn btn-small" event="cancel">Cancel</g:link>
                        <g:link class="btn btn-small" event="back"><i class="icon-chevron-left"></i>&nbsp;Previous</g:link>
                        <g:submitButton name="continue" class="btn btn-small btn-primary" value="Next" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>
