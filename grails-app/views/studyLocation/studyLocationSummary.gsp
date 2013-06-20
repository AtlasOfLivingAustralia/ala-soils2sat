<%@ page import="au.org.ala.soils2sat.AttachmentCategory; org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <r:require module="visualisationHandlers" />
        <meta name="layout" content="detail"/>
        <title>Study Location Summary - ${studyLocationName}</title>
    </head>

    <body>

        %{--<g:set var="visitSummaryLink" value="${createLink(controller: 'studyLocation', action: 'studyLocationVisitsFragment', params: [studyLocationName: studyLocationName])}"/>--}%

        <style type="text/css">

        .fieldColumn {
            width: 300px;
        }

        </style>

        <script type="text/javascript">

            function afterSelectionChanged() {
                refreshVisitsTab();
            }

            function refreshVisitsTab() {
                $("#visitsTab").html("Retrieving Study Location Visits... <sts:spinner/>");
                $.ajax("${createLink(controller: 'studyLocation', action: 'studyLocationVisitsFragment', params: [studyLocationName: studyLocationName])}").done(function (html) {
                    $("#visitsTab").html(html);
                });
            }

            function loadPhotographThumbnails() {
                $("#photoTab").html("Retrieving thumbnails... <sts:spinner/>");
                $.ajax("${createLink(controller: 'studyLocation', action: 'studyLocationPhotoThumbnailsFragment', params: [studyLocationName: studyLocationName])}").done(function (html) {
                    $("#photoTab").html(html);
                });
            }

            $(document).ready(function () {

                //load the environmental data (async)
                $.ajax("${createLink(controller: 'studyLocation', action: 'studyLocationLayersFragment', params: [studyLocationName: studyLocationName])}").done(function (html) {
                    $("#environmentalDataSection").html(html);
                });

                $.ajax("${createLink(controller: 'visualisation', action:'studyLocationVisualisations', params:[studyLocationName: studyLocationName])}").done(function(html) {
                    $("#studyLocationVisualisations").html(html);
                });

                $('a[data-toggle="tab"]').on('shown', function (e) {
                    var tabHref = $(this).attr('href');
                    if (tabHref == "#voucheredTaxaTab") {
                        $("#voucheredTaxaTab").html("Retrieving taxa data for study location visit... <sts:spinner/>");
                        $.ajax("${createLink(controller: 'studyLocation', action: 'studyLocationVoucheredTaxaFragment', params: [studyLocationName: studyLocationName])}").done(function (html) {
                            $("#voucheredTaxaTab").html(html);
                        });
                    } else if (tabHref == "#occurrenceTaxaTab") {
                        $("#occurrenceTaxaTab").html("Retrieving taxa data for study location visit... <sts:spinner/>");
                        $.ajax("${createLink(controller: 'studyLocation', action: 'studyLocationOccurrenceTaxaFragment', params: [studyLocationName: studyLocationName])}").done(function (html) {
                            $("#occurrenceTaxaTab").html(html);
                        });
                    } else if (tabHref == "#visitsTab") {
                        refreshVisitsTab();
                    } else if (tabHref == "#photoTab") {
                        loadPhotographThumbnails();
                    }

                });

                $("#btnDeselect").click(function(e) {
                    e.preventDefault();
                    $.ajax("${createLink(controller:'studyLocation', action:'deselectStudyLocation', params: ['studyLocationName': studyLocationName])}").done(function(e) {
                        window.location = "${createLink(controller: "studyLocation", action: "studyLocationSummary", params: ['studyLocationName': studyLocationName])}";
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
                        <td>
                            <sts:homeBreadCrumb />
                            <sts:navSeperator/>
                            <sts:studyLocationBreadCrumb studyLocationName="${studyLocationName}" nolink="${true}" />
                        </td>
                        <td>
                            <g:if test="${isSelected}">
                                <button id="btnMoveNext" style="margin-right:5px" class="btn btn-small pull-right">Show next selected&nbsp;<i class="icon icon-arrow-right"></i></button>
                                <button id="btnMovePrevious" style="margin-right:5px" class="btn btn-small pull-right"><i class="icon icon-arrow-left"></i>&nbsp;Show previous selected</button>
                            </g:if>
                        </button>
                        </td>
                    </tr>
                </table>
            </legend>

            <div class="well well-small">

                <div class="tabbable">

                    <ul class="nav nav-tabs" style="margin-bottom: 0px">
                        <li class="active"><a href="#detailsTab" data-toggle="tab">Details</a></li>
                        <li><a href="#voucheredTaxaTab" data-toggle="tab">Taxa (Vouchered)</a></li>
                        <li><a href="#occurrenceTaxaTab" data-toggle="tab">Taxa (Occurrence)</a></li>
                        <li><a href="#visitsTab" id="visitsTabLink" data-toggle="tab">Study Location Visits</a></li>
                        <g:if test="${attachmentMap[AttachmentCategory.Photo]}">
                            <li><a href="#photoTab" id="photoTabLink" data-toggle="tab">Photographs</a></li>
                        </g:if>
                    </ul>

                    <div class="tab-content">
                        <div class="tab-pane active" id="detailsTab">
                            <h4>Study Location Details</h4>
                            <table class="table table-bordered table-striped">
                                <tr>
                                    <td class="fieldColumn">Location (Lat, Long)</td>
                                    <td>${studyLocationDetails.longitude}, ${studyLocationDetails.latitude}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Location (UTM)</td>
                                    <td>${studyLocationDetails.easting}, ${studyLocationDetails.northing} (${studyLocationDetails.mgaZone as Integer})</td>
                                </tr>

                                <tr>
                                    <td class="fieldColumn">Bioregion Name</td>
                                    <td>${studyLocationDetails.bioregionName}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Landform element</td>
                                    <td>${studyLocationDetails.landformElement}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Landform pattern</td>
                                    <td>${studyLocationDetails.landformPattern}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Number of distinct plant species</td>
                                    <td>${studyLocationDetails.numberOfDistinctPlantSpecies}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Number of visits</td>
                                    <td>${studyLocationDetails.numberOfVisits}</td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Observers</td>
                                    <td>
                                        <g:set var="observers" value="${studyLocationDetails.observers}"/>
                                        <g:each in="${observers}" var="observer" status="i">
                                            <a href="#">${observer}</a><g:if test="${i < observers.size()-1}">,&nbsp;</g:if>
                                        </g:each>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">First visit date</td>
                                    <td><sts:formatDateStr date="${studyLocationDetails.firstVisitDate}"/></td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Last visit date</td>
                                    <td><sts:formatDateStr date="${studyLocationDetails.lastVisitDate}"/></td>
                                </tr>
                                <tr>
                                    <td class="fieldColumn">Sampling Methods that have been performed at this site</td>
                                    <td>
                                        <ul>
                                            <g:each in="${studyLocationDetails.samplingUnits}" var="unit">
                                                <li><a href="${createLink(action:'studyLocationSamplingUnitDetails', params:[studyLocationName: studyLocationName, samplingUnitTypeId: unit.id])}">${unit.description}</a></li>
                                            </g:each>
                                        </ul>
                                    </td>
                                </tr>
                            </table>

                            <div id="studyLocationVisualisations">
                                <sts:spinner />
                            </div>

                            <h4>Environmental Data</h4>
                            <small>* Determined by the selected layers on map page</small>
                            <div id="environmentalDataSection">
                                <sts:loading message="Loading environmental data" />
                            </div>


                        </div>

                        <div class="tab-pane active" id="voucheredTaxaTab">
                        </div>
                        <div class="tab-pane active" id="occurrenceTaxaTab">
                        </div>

                        <div class="tab-pane" id="visitsTab">
                        </div>

                        <g:if test="${attachmentMap[AttachmentCategory.Photo]}">
                            <div class="tab-pane" id="photoTab">
                            </div>
                        </g:if>

                    </div>
                </div>
            </div>
        </div>
    </body>
</html>