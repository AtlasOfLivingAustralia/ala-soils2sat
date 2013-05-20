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
                    $.ajax("${createLink(controller: 'studyLocation', action:'selectStudyLocationVisit', params:[studyLocationVisitId: visitDetail?.studyLocationVisitId, studyLocationName: studyLocationName])}").done(function() {
                        window.location = "${createLink(controller:'studyLocation', action: 'studyLocationVisitSummary', params:[studyLocationVisitId: visitDetail?.studyLocationVisitId, studyLocationName: studyLocationName])}";
                    });

                });

                $("#btnDeselect").click(function(e) {
                    e.preventDefault();
                    $.ajax("${createLink(controller: 'studyLocation', action:'deselectStudyLocationVisit', params:[studyLocationVisitId: visitDetail?.studyLocationVisitId, studyLocationName: studyLocationName])}").done(function() {
                        window.location = "${createLink(controller:'studyLocation', action: 'studyLocationVisitSummary', params:[studyLocationVisitId: visitDetail?.studyLocationVisitId, studyLocationName: studyLocationName])}";
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
                        Visit ${visitDetail?.studyLocationVisitId}</td>
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
                        <td>${studyLocationDetails?.studyLocationName}</td>
                    </tr>
                    <tr>
                        <td>Start Date</td>
                        <td><sts:formatDateStr date="${visitDetail?.visitStartDate}"/></td>
                    </tr>
                    <tr>
                        <td>End Date</td>
                        <td><sts:formatDateStr date="${visitDetail?.visitEndDate ?: visitDetail.visitStartDate}"/></td>
                    </tr>
                    <tr>
                        <td>Observers</td>
                        <td>${visitDetail?.observers?.collect { it.observerName } ?.join(", ")}</td>
                    </tr>
                    <tr>
                        <td>Visit Notes</td>
                        <td>${visitDetail?.visitNotes}</td>
                    </tr>
                    <g:each in="['climaticCondition', 'disturbance', 'drainageType', 'erosionAbundance', 'erosionType', 'locationDescription', 'microrelief', 'soilObservationType', 'surfaceCoarseFragsAbundance', 'surfaceCoarseFragsLithology','surfaceCoarseFragsSize','surfaceCoarseFragsType', 'vegetationCondition']" var="propName">
                        <tr>
                            <td>${propName}</td>
                            <td>${visitDetail?.getAt(propName)}</td>
                        </tr>
                    </g:each>
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
                    <g:each in="${visitDetail?.samplingUnits}" var="su">
                        <tr>
                            <td></td>
                            <td></td>
                            <td><a href="${createLink(controller: 'studyLocation', action:'samplingUnitDetail', params:[studyLocationVisitId: visitDetail.studyLocationVisitId, samplingUnit: su.id])}">${su.description}</a> </td>
                        </tr>
                    </g:each>
                </table>
            </div>
        </div>
    </body>
</html>