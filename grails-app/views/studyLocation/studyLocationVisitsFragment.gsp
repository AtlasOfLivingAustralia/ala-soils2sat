%{--
- ﻿Copyright (C) 2013 Atlas of Living Australia
- All Rights Reserved.
-
- The contents of this file are subject to the Mozilla Public
- License Version 1.1 (the "License"); you may not use this file
- except in compliance with the License. You may obtain a copy of
- the License at http://www.mozilla.org/MPL/
-
- Software distributed under the License is distributed on an "AS
- IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
- implied. See the License for the specific language governing
- rights and limitations under the License.
--}%

<div>
    <h4>Study Location Visits</h4>
    <table class="table table-bordered table-striped">
        <thead>
            <tr>
                <th>Visit</th>
                <th>Dates</th>
                <th>Observers</th>
                <th></th>
            </tr>
        </thead>
        <g:each in="${visitSummaries}" var="visit">
            <tr studyLocationVisitId="${visit.studyLocationVisitId}">
                <g:set var="samplesURL" value="${createLink(controller: 'studyLocation', action: 'studyLocationVisitSummary', params: [studyLocationName: studyLocationName, studyLocationVisitId: visit.studyLocationVisitId])}" />
                <td><a href="${samplesURL}">${visit.studyLocationVisitId}</a></td>
                <td>Visit&nbsp;Start&nbsp;Date:&nbsp;<sts:formatDateStr date="${visit.visitStartDate}"/><br/>
                    Visit&nbsp;End&nbsp;Date:&nbsp;<sts:formatDateStr date="${visit.visitEndDate}"/>
                </td>
                <td>${visit.observers?.collect({ it.observerName })?.join(", ")}</td>
                <td>
                    <g:if test="${appState.selectedVisits?.find { it.studyLocationVisitId == visit.studyLocationVisitId }}">
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
