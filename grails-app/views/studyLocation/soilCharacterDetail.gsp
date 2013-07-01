<%@ page import="au.org.ala.soils2sat.SoilColorUtils; org.apache.commons.lang.WordUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <meta name="layout" content="detail"/>
        <title>Sampling Unit details - ${studyLocationDetail?.studyLocationName} - Visit <sts:formatDateStr date="${visitDetail.visitStartDate}" />  - ${samplingUnitName}</title>
    </head>

    <body>

        <script type="text/javascript">

            $(document).ready(function() {
                $("#btnExportData").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(controller:'studyLocation', action:'downloadSamplingUnit', params:[studyLocationVisitId: visitDetail.studyLocationVisitId, samplingUnitTypeId:samplingUnitTypeId])}";
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
                            <sts:studyLocationBreadCrumb studyLocationName="${studyLocationDetail.studyLocationName}" />
                            <sts:navSeperator/>
                            <sts:studyLocationVisitBreadCrumb studyLocationVisitId="${visitDetail.studyLocationVisitId}" />
                            <sts:navSeperator/>
                            <span class="s2s-breadcrumb">${samplingUnitName}</span>
                        </td>
                        <td>
                            <button class="btn btn-small pull-right" id="btnExportData"><i class="icon-download"></i>&nbsp;Export</button>
                        </td>
                    </tr>
                </table>
            </legend>

            <h4>Sampling Unit - ${samplingUnitName}</h4>

            <g:each in="${groups}" var="kvp">
                <h5>${kvp.key}</h5>
                <g:if test="${kvp.key == 'Colour'}">
                    <small>* Colours shown are approximate, and based on a conversion from Munsell Renotation data to sRGB. Colour accuracy will be affected by monitor calibration</small>
                </g:if>
                <small>
                    <table class="table table-bordered table-striped table-condensed">
                        <thead>
                            <tr>
                                <th>Depth</th>
                                <g:each in="${kvp.value}" var="property">
                                    <th><sts:formatSamplingUnitColumn code="${property}" /></th>
                                </g:each>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${dataList}" var="row">
                                <tr>
                                    <td>${row.upperDepth} - ${row.lowerDepth}</td>
                                    <g:each in="${kvp.value}" var="property">
                                        <td>
                                            <g:if test="${kvp.key == 'Colour' && ['colourWhenDry', 'colourWhenMoist'].contains(property)}">
                                                <g:set var="mc" value="${SoilColorUtils.parseMunsell(row[property] as String)}" />
                                                <g:if test="${mc}">
                                                    <span style="background-color: rgb(${mc.red}, ${mc.green}, ${mc.blue}); width: 50px; display: inline-block">&nbsp;</span>
                                                </g:if>
                                            </g:if>
                                            <sts:renderSamplingUnitValue value="${row[property]}" />
                                        </td>
                                    </g:each>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                </small>
            </g:each>

        </div>
    </body>
</html>