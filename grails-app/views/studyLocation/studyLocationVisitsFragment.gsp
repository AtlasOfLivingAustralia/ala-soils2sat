<div>
    <h4>Study Location Visits</h4>
    <table class="table table-bordered table-striped">
        <thead>
            <tr>
                <th>Visit</th>
                <th>Dates</th>
                <th>Observers</th>
                <th>Sampling activities</th>
                <th></th>
            </tr>
        </thead>
        <g:each in="${visitSummaries}" var="visit">
            <tr studyLocationVisitId="${visit.visitId}">
                <g:set var="samplesURL" value="${createLink(controller: 'studyLocation', action: 'studyLocationVisitSummary', params: [studyLocationName: studyLocationName, studyLocationVisitId: visit.visitId])}" />
                <td><a href="${samplesURL}">${visit.visitId}</a></td>
                <td>Visit&nbsp;Start&nbsp;Date:&nbsp;<sts:formatDateStr date="${visit.startDate}"/><br/>
                    Visit&nbsp;End&nbsp;Date:&nbsp;<sts:formatDateStr date="${visit.endDate}"/>
                </td>
                <td>${visit.observers?.join(", ")}</td>
                <td>
                    <ul>
                        <g:each in="${visit.samplingUnitNames}" var="samplingUnitCode">
                            <li><a href="#"><sts:formatSamplingUnitName code="${samplingUnitCode}"/></a></li>
                        </g:each>
                    </ul>
                </td>
                <td>
                    <g:if test="${appState.selectedVisits?.find { it.studyLocationVisitId == visit.visitId }}">
                        <button class="btnDeselect btn btn-small btn-warning pull-right">Deselect</button>
                    </g:if>
                    <g:else>
                        <button class="btnSelect btn btn-small btn-info pull-right">Select</button>
                    </g:else>
                </td>
            </tr>
        </g:each>
    </table>
</div>

<script type="text/javascript">

    $(document).ready(function() {

        $(".btnSelect").click(function(e) {
            e.preventDefault();
            var visitId = $(this).parents("[studyLocationVisitId]").attr("studyLocationVisitId");
            if (visitId) {
                $.ajax("${createLink(controller: 'studyLocation', action:'selectStudyLocationVisit', params:[studyLocationName: studyLocationName])}&studyLocationVisitId=" + visitId).done(function() {
                    if (afterSelectionChanged) {
                        afterSelectionChanged();
                    }
                });
            }
        });

        $(".btnDeselect").click(function(e) {
            e.preventDefault();
            var visitId = $(this).parents("[studyLocationVisitId]").attr("studyLocationVisitId");
            if (visitId) {
                $.ajax("${createLink(controller: 'studyLocation', action:'deselectStudyLocationVisit')}?studyLocationVisitId=" + visitId).done(function() {
                    if (afterSelectionChanged) {
                        afterSelectionChanged();
                    }
                });
            }
        });

    });

</script>
