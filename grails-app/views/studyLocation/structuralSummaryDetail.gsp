<%@ page import="au.org.ala.soils2sat.VisualisationUtils; org.apache.commons.lang.WordUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
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

            <g:set var="detail" value="${dataList[0]}" />
            <g:set var="fields" value="['upper1Dominant','upper2Dominant','upper3Dominant','mid1Dominant','mid2Dominant','mid3Dominant','ground1Dominant','ground2Dominant','ground3Dominant']" />
            <g:set var="colors" value="${au.org.ala.soils2sat.VisualisationUtils.structualSummaryColors}" />
            <g:set var="colorIndex" value="${-1}" />

            <h4>Sampling Unit - ${samplingUnitName}</h4>
            <div>
                <table class="table table-bordered">
                    <tr>
                        <td>
                            <strong>Description:</strong><br/>
                            <sts:renderSamplingUnitValue value="${detail?.description}" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <strong>Phenology Comment:</strong><br/>
                            <sts:renderSamplingUnitValue value="${detail?.phenologyComment}" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <strong>Mass Flowing Event:</strong><br/>
                            <sts:renderSamplingUnitValue value="${detail?.massFloweringEvent}" />
                        </td>
                    </tr>
                </table>
            </div>

            <table class="table table-bordered table-striped">
                <g:each in="${fields}" var="field">
                    <tr>
                        <td style="color: ${colors[++colorIndex]};width: 200px">${field}</td>
                        <td><sts:taxaHomePageLink name="${detail[field]}" ifEmpty="N/A"/></td>
                    </tr>
                </g:each>
            </table>

        </div>
    </body>
</html>