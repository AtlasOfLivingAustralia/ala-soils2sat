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