<%@ page import="org.apache.commons.lang.WordUtils" %>
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

            $(document).ready(function(e) {

                var pit = $("#pointInterceptType");

                pit.change(function() {
                    plotValues($(this).val());
                });

                plotValues(pit.val());

            });

            function plotValues(pointInterceptType) {
                var imageUrl = "${createLink(controller:'studyLocation', action:'pointInterceptImage', params:[studyLocationVisitId: visitDetail.studyLocationVisitId])}&pointInterceptType=" + pointInterceptType;
                $("#pointInterceptImage").html('<img src="' + imageUrl + '">')
            }



        </script>

        <div class="container-fluid">
            <legend>
                <table style="width:100%">
                    <tr>
                        <td>
                            <a href="${createLink(controller:'map', action:'index')}">Map</a><sts:navSeperator/>
                            <a href="${createLink(controller: 'studyLocation', action: 'studyLocationSummary', params: [studyLocationName: studyLocationDetail.studyLocationName])}">${studyLocationDetail.studyLocationName}</a><sts:navSeperator/>
                            <a href="${createLink(controller: 'studyLocation', action: 'studyLocationVisitSummary', params: [studyLocationVisitId: visitDetail.studyLocationVisitId])}">Visit ${visitDetail.studyLocationVisitId}</a><sts:navSeperator/>
                            ${samplingUnitName}</td>
                        <td></td>
                    </tr>
                </table>
            </legend>

            <h4>Sampling Unit - ${samplingUnitName}</h4>

            <div class="well">
                Plot Point Intercept values for:
                <g:select id="pointInterceptType" from="${['substrate', 'herbariumDetermination', 'growthForm']}" name="pointInterceptType" />
                <br />
                <div id="pointInterceptImage">
                </div>
            </div>

            <small>
                <table class="table table-bordered table-striped table-condensed">
                    <thead>
                        <tr>
                            <g:each in="${columnHeadings}" var="colHeading">
                                <th>${colHeading}</th>
                            </g:each>
                        </tr>
                    </thead>
                    <tbody>
                        <g:each in="${dataList}" var="row">
                            <tr>
                                <g:each in="${columnHeadings}" var="colHeading">
                                    <td>${row[colHeading]}</td>
                                </g:each>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
            </small>
        </div>
    </body>
</html>