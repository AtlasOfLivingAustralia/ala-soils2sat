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
                    <tr>
                        <td class="studyLocationSearchResultRow" studyLocationName="${result.siteName}">${result.siteName} (${result.zone} ${result.easting} ${result.northing})</td>
                        <td>[ <a href="${createLink(controller: 'studyLocation', action: 'studyLocationSummary', params: [studyLocationName: result.siteName])}">View summary</a> ]
                        </td>
                        <td>
                            <g:set var="selected" value="${!appState.containsPlot(result.siteName)}"/>
                            <button style="display:${selected ? 'block' : 'none'}" class="btn btn-mini selectSearchResult pull-right" studyLocationName="${result.siteName}">Select</button>
                            <button style="display:${selected ? 'none' : 'block'}" class="btn btn-mini btn-warning deselectSearchResult pull-right" studyLocationName="${result.siteName}">Deselect</button>
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
        var studyLocationName = $(this).attr("studyLocationName");
        if (studyLocationName) {
            $(this).css("display", "none");
            $(this).siblings(".deselectSearchResult").css("display", "block");
            selectPlot(studyLocationName, function () {
                renderSelectedList();
            });

        }
    });

    $(".deselectSearchResult").click(function (e) {
        e.preventDefault();
        var studyLocationName = $(this).attr("studyLocationName");
        if (studyLocationName) {
            $(this).css("display", "none");
            $(this).siblings(".selectSearchResult").css("display", "block");
            deselectPlot(studyLocationName, function () {
                renderSelectedList();
            });
        }

    });

    $("#btnSelectAllSearchResults").click(function (e) {
        var studyLocationNames = [];
        $(".selectSearchResult").each(function () {
            var studyLocationName = $(this).attr("studyLocationName");
            if (studyLocationName) {
                studyLocationNames.push(studyLocationName);
            }
        });

        selectPlots(studyLocationNames, function () {
            renderSelectedList();
            $(".selectSearchResult.selectSearchResult").each(function () {
                $(this).css("display", "none");
                $(this).siblings(".deselectSearchResult").css("display", "block");
            });
        });


    });

</script>