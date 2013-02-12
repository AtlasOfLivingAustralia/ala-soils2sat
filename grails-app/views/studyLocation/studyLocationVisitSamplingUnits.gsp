<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="detail"/>
        <title>Study Location Visit Summary - ${studyLocationName}</title>
    </head>

    <body>
        <div class="container-fluid">
            <legend>
                <table style="width:100%">
                    <tr>
                        <td>Study Location&nbsp;&#187;&nbsp;
                            <a href="${createLink(controller: 'studyLocation', action: 'studyLocationSummary', params: [studyLocationName: studyLocationName])}">${studyLocationName}</a>&nbsp;&#187;&nbsp;
                            <a href="${createLink(controller: 'studyLocation', action: 'studyLocationVisitSummary', params: [studyLocationName: studyLocationName])}">Visits</a>&nbsp;&#187;&nbsp;
                        Visit ${visitSummary.visitId} Sampling Units</td>
                        <td></td>
                    </tr>
                </table>
            </legend>

            <div class="well well-small">

                <h4>Study Location Visit Summary</h4>
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
                            <td><a href="${createLink(controller: 'studyLocation', action:'samplingUnitDetail', params:[studyLocationName: studyLocationName, visitId: visitSummary.visitId, samplingUnit: su.samplingUnit])}">${su.description ?: su.samplingUnit}</a> </td>
                        </tr>
                    </g:each>
                </table>
            </div>
        </div>
    </body>
</html>