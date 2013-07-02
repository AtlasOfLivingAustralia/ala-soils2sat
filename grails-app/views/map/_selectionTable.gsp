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

<table class="table table-bordered table-condensed">

<g:set var="model" value="${appState.selectedVisits.groupBy {it.studyLocationName}}" />
    <g:if test="${model}">
        <g:each in="${model.keySet()}" var="studyLocation">
            <tr class="warning" studyLocationName="${studyLocation}">
                <td colspan="2"><a class="studyLocationDetailsLink" href="#"><i class="icon-map-marker"></i>&nbsp;${studyLocation}</a></td>
            </tr>
            <g:each in="${model[studyLocation]}" var="obj">
                <g:set var="visit" value="${obj}" />
                <tr studyLocationVisitId="${visit.studyLocationVisitId}" studyLocationName="${visit.studyLocationName}">
                    <td><sts:navSeperator />&nbsp;
                        <a class="studyLocationVisitDetailsLink" href="#"><sts:formatVisitLabel studyLocationVisitId="${visit.studyLocationVisitId}" /></a>
                        <button class="btn btn-mini pull-right btnRemoveSelectedVisit" title="Remove study location visit"><i class="icon-remove"/></button>
                    </td>
                </tr>
            </g:each>
        </g:each>
    </g:if>
    <g:else>
        <tr>
            <td>No study location visits have been selected</td>
        </tr>
    </g:else>
</table>

<script type="text/javascript">

    $(".studyLocationVisitDetailsLink").click(function(e) {
        e.preventDefault();
        var studyLocationVisitId = $(this).parents('[studyLocationVisitId]').attr("studyLocationVisitId");
        var studyLocationName = $(this).parents('[studyLocationName]').attr("studyLocationName");
        if (studyLocationVisitId && studyLocationName) {
            showVisitDetails(studyLocationName, studyLocationVisitId);
        }
    });

    $(".studyLocationDetailsLink").click(function (e) {
        e.preventDefault();
        var studyLocationName = $(this).parents('[studyLocationName]').attr("studyLocationName");
        showPlotDetails(studyLocationName);
    });

    $(".btnRemoveSelectedVisit").click(function (e) {
        e.preventDefault();
        var studyLocationVisitId = $(this).parents('[studyLocationVisitId]').attr("studyLocationVisitId");
        if (studyLocationVisitId) {
            deselectVisit(studyLocationVisitId);
        }
    });

    function showVisitDetails(studyLocationName, studyLocationVisitId) {
        if (studyLocationVisitId && studyLocationName) {
            window.location = "${createLink(controller: 'studyLocation', action:'studyLocationVisitSummary', )}?studyLocationVisitId=" + studyLocationVisitId + "&studyLocationName=" + studyLocationName
            return true;
        }
    }

    function showPlotDetails(studyLocationName) {
        if (studyLocationName) {
            window.location = "${createLink(controller: 'studyLocation', action:'studyLocationSummary', )}?studyLocationName=" + studyLocationName
            return true;
        }
    }

</script>
