<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="detail"/>
        <title>Study Location Summary - ${studyLocationName}</title>
    </head>

    <body>

        <g:set var="visitSummaryLink" value="${createLink(controller: 'studyLocation', action: 'studyLocationVisitSummary', params: [studyLocationName: studyLocationName])}"/>

        <style type="text/css">

        .tab-content {
            border-left: 1px solid #d3d3d3;
            border-right: 1px solid #d3d3d3;
            border-bottom: 1px solid #d3d3d3;
            padding: 10px;
            background-color: white;
        }

        .fieldColumn {
            width: 300px;
        }

        </style>

        <script type="text/javascript">

            $(document).ready(function () {

                $('a[data-toggle="tab"]').on('shown', function (e) {
                    $("#environmentalLayersTab").html("");
                    var tabHref = $(this).attr('href');
                    if (tabHref == '#environmentalLayersTab') {
                        $("#environmentalLayersTab").html("Retrieving data for study location... <sts:spinner/>");
                        $.ajax("${createLink(controller: 'studyLocation', action: 'studyLocationLayersFragment', params: [studyLocationName: studyLocationName])}").done(function (html) {
                            $("#environmentalLayersTab").html(html);
                        });
                    } else if (tabHref == "#taxaTab") {
                        $("#taxaTab").html("Retrieving taxa data for study location... <sts:spinner/>");
                        $.ajax("${createLink(controller: 'studyLocation', action: 'studyLocationTaxaFragment', params: [studyLocationName: studyLocationName])}").done(function (html) {
                            $("#taxaTab").html(html);
                        });
                    }
                });

                $("#btnViewVisitSummaries").click(function (e) {
                    e.preventDefault();
                    window.location = "${visitSummaryLink}";
                });

                $("#btnDeselect").click(function(e) {
                    e.preventDefault();
                    $.ajax("${createLink(controller:'studyLocation', action:'deselectStudyLocation', params: ['studyLocationName': studyLocationName])}").done(function(e) {
                        window.location = "${createLink(controller: "studyLocation", action: "studyLocationSummary", params: ['studyLocationName': studyLocationName])}";
                    });
                });

                $("#btnDeselectAndReturn").click(function(e) {
                    e.preventDefault();
                    $.ajax("${createLink(controller:'studyLocation', action:'deselectStudyLocation', params: ['studyLocationName': studyLocationName])}").done(function(e) {
                        window.location = "${createLink(controller: "map", action: "index")}";
                    });
                });

                $("#btnSelect").click(function(e) {
                    e.preventDefault();
                    $.ajax("${createLink(controller:'studyLocation', action:'selectStudyLocation', params: ['studyLocationName': studyLocationName])}").done(function(e) {
                        window.location = "${createLink(controller: "studyLocation", action: "studyLocationSummary", params: ['studyLocationName': studyLocationName])}";
                    });
                });

                $("#btnSelectAndReturn").click(function(e) {
                    e.preventDefault();
                    $.ajax("${createLink(controller:'studyLocation', action:'selectStudyLocation', params: ['studyLocationName': studyLocationName])}").done(function(e) {
                        window.location = "${createLink(controller: "map", action: "index")}";
                    });
                });

                $("#btnMoveNext").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(controller: "studyLocation", action: "nextSelectedStudyLocationSummary", params:[studyLocationName: studyLocationName])}";
                });

                $("#btnMovePrevious").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(controller: "studyLocation", action: "previousSelectedStudyLocationSummary", params:[studyLocationName: studyLocationName])}";
                });

            });

        </script>

        <div class="container-fluid">
            <legend>
                <table style="width:100%">
                    <tr>
                        <td><a href="${createLink(controller:'map', action:'index')}">Map</a>&nbsp;&#187;&nbsp;Study Location Summary&nbsp;&#187;&nbsp;${studyLocationName}</td>
                        <td>
                            <button id="btnViewVisitSummaries" class="btn btn-small pull-right">View Visit Summaries (${studyLocationSummary.data.numVisits})
                            <g:if test="${isSelected}">
                                <button id="btnDeselectAndReturn" style="margin-right:5px" class="btn btn-small btn-warning pull-right">Deselect and return to map</button>
                                <button id="btnDeselect" style="margin-right:5px" class="btn btn-small btn-warning pull-right">Deselect study location</button>
                                <button id="btnMoveNext" style="margin-right:5px" class="btn btn-small pull-right">Show next selected&nbsp;<i class="icon icon-arrow-right"></i></button>
                                <button id="btnMovePrevious" style="margin-right:5px" class="btn btn-small pull-right"><i class="icon icon-arrow-left"></i>&nbsp;Show previous selected</button>
                            </g:if>
                            <g:else>
                                <button id="btnSelectAndReturn" style="margin-right:5px" class="btn btn-small btn-info pull-right">Select and return to map</button>
                                <button id="btnSelect" style="margin-right:5px" class="btn btn-small btn-info pull-right">Select study location</button>
                            </g:else>
                        </button>
                        </td>
                    </tr>
                </table>
            </legend>

            <div class="well well-small">

                <div class="tabbable">

                    <ul class="nav nav-tabs" style="margin-bottom: 0px">
                        <li class="active"><a href="#detailsTab" data-toggle="tab">Details</a></li>
                        <li><a href="#environmentalLayersTab" data-toggle="tab">Environmental data</a></li>
                        <li><a href="#taxaTab" data-toggle="tab">Taxa data</a></li>
                    </ul>

                    <div class="tab-content">
                        <div class="tab-pane active" id="detailsTab">
                            <h4>Study Location Details</h4>
                            <table class="table table-bordered table-striped">
                                <tr>
                                    <td class="fieldColumn">Location (Lat, Long)</td>
                                    <td>${studyLocationSummary.longitude}, ${studyLocationSummary.latitude}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Location (UTM)</td>
                                    <td>${studyLocationSummary.data.easting}, ${studyLocationSummary.data.northing} (${studyLocationSummary.data.zone as Integer})</td>
                                </tr>

                                <tr>
                                    <td class="fieldColumn">Bioregion Name</td>
                                    <td>${studyLocationSummary.data.bioregionName}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Landform element</td>
                                    <td>${studyLocationSummary.data.landformElement}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Landform pattern</td>
                                    <td>${studyLocationSummary.data.landformPattern}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Number of distinct plant species (Unverified)</td>
                                    <td>${studyLocationSummary.data.numDistinctPlantSpeciesUnverified}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Number of distinct plant species (Verified)</td>
                                    <td>${studyLocationSummary.data.numDistinctPlantSpeciesVerified}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Total number of distinct plant species</td>
                                    <td>${studyLocationSummary.data.numDistinctPlantSpeciesTotal}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Number of visits</td>
                                    <td><a href="${visitSummaryLink}">${studyLocationSummary.data.numVisits}</a></td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Observers</td>
                                    <td>
                                        <g:set var="observers" value="${studyLocationSummary.observers}"/>
                                        <g:each in="${observers}" var="observer" status="i">
                                            <a href="#">${observer}</a><g:if test="${i < observers.size()-1}">,&nbsp;</g:if>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">First visit date</td>
                                    <td><sts:formatDateStr date="${studyLocationSummary.firstVisitDate}"/></td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Last visit date</td>
                                    <td><sts:formatDateStr date="${studyLocationSummary.lastVisitDate}"/></td>
                                </tr>
                                %{--<tr>--}%
                                %{--<td class="fieldColumn">Number of sampling units</td>--}%
                                %{--<td>${studyLocationSummary.data.numSamplingUnits}</td>--}%
                                %{--</tr>--}%
                                <tr>
                                    <td class="fieldColumn">Sampling Methods that have been performed at this site</td>
                                    <td>
                                        <ul>
                                            <g:each in="${studyLocationSummary.data.samplingUnitTypeList}" var="unit">
                                                <li><a href="#"><sts:formatSamplingUnitName code="${unit}"/></a></li>
                                            </g:each>
                                        </ul>
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div class="tab-pane" id="environmentalLayersTab">
                        </div>

                        <div class="tab-pane" id="taxaTab">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>