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

<g:set var="deselectVisible" value="${appState?.selectedPlotNames?.find { it == studyLocationName }}" />

<div>
    <div id="studyLocationSummaryPanel" class="well well-small">
        Retrieving Study Location data...
    </div>


    <button class="btn btn-small btn-warning" id="btnDeselectPlot">Remove from selection</button>
    <button class="btn btn-small btn-primary" id="btnSelectPlot">Add to selection</button>
    <button class="btn btn-small" id="btnStudyLocationSummary">View Summary</button>


</div>

<script type="text/javascript">

    <g:if test="${deselectVisible}">
        $("#btnSelectPlot").css("display", "none");
        $("#btnDeselectPlot").css("display", "inline-block");
    </g:if>
    <g:else>
        $("#btnSelectPlot").css("display", "inline-block");
        $("#btnDeselectPlot").css("display", "none");
    </g:else>

    $("#btnSelectPlot").click(function (e) {
        e.preventDefault();
        selectPlot("${studyLocationName}");
        $("#btnSelectPlot").css("display", "none");
        $("#btnDeselectPlot").css("display", "inline-block");

    });

    $("#btnDeselectPlot").click(function (e) {
        e.preventDefault();
        deselectPlot("${studyLocationName}");
        $("#btnSelectPlot").css("display", "inline-block");
        $("#btnDeselectPlot").css("display", "none");
    });

    $("#btnStudyLocationSummary").click(function (e) {
        e.preventDefault();
        window.open("${createLink(controller:'studyLocation', action:'studyLocationSummary', params:[studyLocationName: studyLocationName])}", "StudyLocationSummary");
    });

    $(document).ready(function (e) {
        $.ajax("${createLink(controller:'studyLocation', action:'studyLocationDataFragment', params: [studyLocationName: studyLocationName])}").done(function (html) {
            $("#studyLocationSummaryPanel").html(html);
        });
    });

</script>