<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="detail"/>
        <title>Species Analysis</title>
    </head>

    <body>

        <style type="text/css">
        </style>

        <script type="text/javascript">

            $(document).ready(function () {
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
                            <h4>Analysis of <i>${speciesName}</i> for selected study locations</h4>
                            <table class="table table-bordered" style="background-color: rgb(249, 249, 249)">
                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'speciesAnalysisLatitude', params:[studyLocationNames: studyLocationNames?.join(","), speciesName:speciesName])}"></div>
                                    </td>
                                </tr>

                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'speciesAnalysisPointInterceptCounts', params:[studyLocationNames: studyLocationNames?.join(","), speciesName:speciesName])}"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'speciesAnalysisSoilPh', params:[studyLocationNames: studyLocationNames?.join(","), speciesName:speciesName])}"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'compareScalarLayer', params:[layerName:'rhu215_m', color: '#4AACC7'])}"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'compareScalarLayer', params:[layerName:'rainm', color: '#F69646'])}"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'compareScalarLayer', params:[layerName:'arid_mean', color: '#C0504D'])}"></div>
                                    </td>
                                </tr>
                            </table>
                            <div>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>&nbsp;</th>
                                            <g:each in="${studyLocationNames}" var="studyLocation">
                                                <th>${studyLocation}</th>
                                            </g:each>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:each in="${layerData.fieldNames}" var="layerName">
                                            <tr>
                                                <td><strong>${layerName}</strong></td>
                                                <g:each in="${studyLocationNames}" var="studyLocation">
                                                    <td>${layerData.data[studyLocation][layerName]}</td>
                                                </g:each>

                                            </tr>
                                        </g:each>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <g:link class="btn btn-small" event="back"><i class="icon-chevron-left"></i>&nbsp;Previous</g:link>
                        <g:link class="btn btn-small btn-primary" event="finish">Return to Map</g:link>
                    </g:form>
                </div>
            </div>
        </div>
        <script type="text/javascript">

            $(".visualisation").each(function() {
                var visLink =$(this).attr("visLink");
                if (visLink) {
                    $(this).html('<img src="../images/spinner.gif" />&nbsp;Loading...');
                    var targetElement = $("[visLink='" + visLink + "']");
                    $.ajax(visLink).done(function(html) {
                        targetElement.html(html);
                    });
                }
            });

        </script>
    </body>
</html>
