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

<div class="well well-small">
    <table style="width: 100%">
        <tr>
            <td>
                <h3>Search Results</h3>
            </td>
            <td>
                <g:if test="${results}">
                    <button id="btnSelectAllSearchResults" class="btn btn-small btn-info pull-right">Select all</button>
                </g:if>
            </td>
        </tr>
    </table>

    <g:if test="${!results}">
        Your search returned no results.
    </g:if>
    <g:else>
        <div>
            ${results.size()} Study Locations found
        </div>

        <div class="well well-small">
            <table class="table table-striped table-hover">
                <g:each in="${results}" var="result">
                    <tr class="studyLocationSearchResultRow" studyLocationName="${result.studyLocationName}" studyLocationVisitId="${result.studyLocationVisitId}">
                        <td><span class="label label-info">${result.studyLocationName}</span> ${result.visitStartDate} <span class="label">${result.observers.collect({ it.observerName })?.join(", ")}</span> <small>(${result.point.latitude}, ${result.point.longitude})</small> </td>
                        <td>[ <a href="${createLink(controller: 'studyLocation', action: 'studyLocationVisitSummary', params: [studyLocationName: result.studyLocationName, studyLocationVisitId: result.studyLocationVisitId])}">View Visit Summary</a> ]
                        </td>
                        <td>
                            <g:set var="selected" value="${!appState.containsVisit(result.studyLocationVisitId as String)}"/>
                            <button style="display:${selected ? 'block' : 'none'}" class="btn btn-mini selectSearchResult pull-right" >Select Visit</button>
                            <button style="display:${selected ? 'none' : 'block'}" class="btn btn-mini btn-warning deselectSearchResult pull-right">Deselect Visit</button>
                        </td>
                    </tr>
                </g:each>
            </table>
        </div>
    </g:else>
</div>

<script type="text/javascript">

    $(".selectSearchResult").click(function (e) {
        e.preventDefault();
        var studyLocationName = $(this).parents("[studyLocationName]").attr("studyLocationName");
        var studyLocationVisitId = $(this).parents("[studyLocationVisitId]").attr("studyLocationVisitId");
        if (studyLocationName && studyLocationVisitId) {
            $(this).css("display", "none");
            $(this).siblings(".deselectSearchResult").css("display", "block");
            selectVisit(studyLocationName, studyLocationVisitId, function () {
                renderSelectedList();
            });

        }
    });

    $(".deselectSearchResult").click(function (e) {
        e.preventDefault();
        var studyLocationVisitId = $(this).parents("[studyLocationVisitId]").attr("studyLocationVisitId");
        if (studyLocationVisitId) {
            $(this).css("display", "none");
            $(this).siblings(".selectSearchResult").css("display", "block");
            deselectVisit(studyLocationVisitId, function () {
                renderSelectedList();
            });
        }

    });

    $("#btnSelectAllSearchResults").click(function (e) {
        var visitList = [];
        $(".selectSearchResult").each(function () {
            var studyLocationVisitId = $(this).parents("[studyLocationVisitId]").attr("studyLocationVisitId");
            if (studyLocationVisitId) {
                visitList.push(studyLocationVisitId);
            }
        });

        selectVisits(visitList, function () {
            renderSelectedList();
            $(".selectSearchResult.selectSearchResult").each(function () {
                $(this).css("display", "none");
                $(this).siblings(".deselectSearchResult").css("display", "block");
            });
        });


    });

</script>