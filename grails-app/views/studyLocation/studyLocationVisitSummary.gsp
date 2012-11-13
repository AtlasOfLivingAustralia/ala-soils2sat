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
                        Visit Summary</td>
                        <td></td>
                    </tr>
                </table>
            </legend>

            <div class="well well-small">
                <h4>Study Location Visits</h4>
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>Visit</th>
                            <th>Dates</th>
                            <th>Observers</th>
                            <th>Sampling units</th>
                        </tr>
                    </thead>
                    <g:each in="${studyLocation.data.visitSummaryList}" var="visit">
                        <tr>
                            <g:set var="samplesURL"
                                   value="${createLink(controller: 'studyLocation', action: 'studyLocationVisitSamplingUnits', params: [studyLocationName: studyLocationName, visitId: visit.visitId])}"/>
                            <td><a href="${samplesURL}">${visit.visitId}</a></td>
                            <td><a
                                href="${samplesURL}">${visit.startDate ?: studyLocation.data.firstVisitDate} - ${visit.endDate ?: studyLocation.data.lastVisitDate}</a>
                            </td>
                            <td>${visit.observerList?.collect({ it.name })?.join(", ")}</td>
                            <td>${visit.samplingUnitNameList?.join(", ")}</td>
                        </tr>
                    </g:each>
                </table>
            </div>
        </div>
    </body>
</html>
