<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="detail"/>
        <title>Study Location Visit Summary - ${studyLocationName}</title>
    </head>

    <body>

        <script type="text/javascript">

            $(document).ready(function() {

                $("#btnSelect").click(function(e) {
                    e.preventDefault();
                    $.ajax("${createLink(controller: 'studyLocation', action:'selectStudyLocationVisit', params:[studyLocationVisitId: visitSummary.visitId, studyLocationName: studyLocationName])}").done(function() {
                        window.location = "${createLink(controller:'studyLocation', action: 'studyLocationVisitSamplingUnits', params:[studyLocationVisitId: visitSummary.visitId, studyLocationName: studyLocationName])}";
                    });

                });

                $("#btnDeselect").click(function(e) {
                    e.preventDefault();
                    $.ajax("${createLink(controller: 'studyLocation', action:'deselectStudyLocationVisit', params:[studyLocationVisitId: visitSummary.visitId, studyLocationName: studyLocationName])}").done(function() {
                        window.location = "${createLink(controller:'studyLocation', action: 'studyLocationVisitSamplingUnits', params:[studyLocationVisitId: visitSummary.visitId, studyLocationName: studyLocationName])}";
                    });
                });
            });

        </script>

        <div class="container-fluid">
            <legend>
                <table style="width:100%">
                    <tr>
                        <td>
                            <a href="${createLink(controller:'map', action:'index')}">Map</a><sts:navSeperator/>
                            <a href="${createLink(controller: 'studyLocation', action: 'studyLocationSummary', params: [studyLocationName: studyLocationName])}">${studyLocationName}</a><sts:navSeperator/>
                            <a href="${createLink(controller: 'studyLocation', action: 'studyLocationVisitSummary', params: [studyLocationName: studyLocationName])}">Visits</a><sts:navSeperator/>
                        Visit ${visitSummary.visitId}</td>
                        <td></td>
                    </tr>
                </table>
            </legend>

            <div class="well well-small">

                <table style="width: 100%">
                    <tr>
                        <td>
                            <h4>Study Location Visit Summary</h4>
                        </td>
                        <td>
                            <div style="float: right">
                                <g:if test="${isSelected}">
                                    <button id="btnDeselect" style="margin-right:5px" class="btn btn-small btn-warning pull-right">Deselect study location visit</button>
                                </g:if>
                                <g:else>
                                    <button id="btnSelect" style="margin-right:5px" class="btn btn-small btn-info pull-right">Select study location visit</button>
                                </g:else>
                            </div>
                        </td>
                    </tr>
                </table>

                <table class="table table-bordered table-striped">
                    <tr>
                        <td>Study Location</td>
                        <td>${studyLocationSummary.name}</td>
                    </tr>
                    <tr>
                        <td>Start Date</td>
                        <td><sts:formatDateStr date="${visitDetail.startDate}"/></td>
                    </tr>
                    <tr>
                        <td>End Date</td>
                        <td><sts:formatDateStr date="${visitDetail.endDate}"/></td>
                    </tr>
                    <tr>
                        <td>Observers</td>
                        <td>${visitSummary.observers?.join(", ")}</td>
                    </tr>
                    <tr>
                        <td>Visit Notes</td>
                        <td>${visitDetail.visitNotes}</td>
                    </tr>

                </table>

                <h4>Study Location Visit Sampling Units</h4>
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Observers</th>
                            <th>Sampling Unit</th>
                        </tr>
                    </thead>
                    <g:each in="${visitSummary.samplingUnitSummaryList}" var="su">
                        <tr>
                            <td><sts:formatDateStr date="${su.sampleDate}"/></td>
                            <td>${su.observerNames?.join(", ")}</td>
                            <td><a href="${createLink(controller: 'studyLocation', action:'samplingUnitDetail', params:[studyLocationName: studyLocationName, studyLocationVisitId: visitSummary.visitId, samplingUnit: su.samplingUnit])}">${su.description ?: su.samplingUnit}</a> </td>
                        </tr>
                    </g:each>
                </table>
            </div>
        </div>
    </body>
</html>