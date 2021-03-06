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

<!doctype html>
<html>
<head>
    <r:require module='openlayers'/>
    <meta name="layout" content="detail"/>
    <title>Find Study Locations</title>\
</head>

<body>

<script src="http://maps.google.com/maps/api/js?key=AIzaSyDAqE0sOMHNienA0zFIUMlY53ztEDiv0d8&sensor=true" type="text/javascript"></script>
<link rel="stylesheet" href="${resource(dir: 'js/openlayers/theme/default', file: 'style.css')}" type="text/css">


<style type="text/css">

.fieldColumn {
    width: 300px;
}

.olLayerGooglePoweredBy {
    display: none;
}

</style>

<script type="text/javascript">

    var map;

    function showMap() {
        initMap();
        refreshStudyLocationPoints();
    }

    function doSearch() {
        var formData = $("#searchForm").serialize();
        $("#searchResultsDiv").html("Searching... <sts:spinner/>");
        var url = "${createLink(action:'findStudyLocationVisitsResultsFragment')}";
        $.post(url, formData, function (content) {
            $("#searchResultsDiv").html(content);
        });
    }

    $(document).ready(function () {

        $("#searchText").focus();

        $("#btnSearch").click(function (e) {
            e.preventDefault();
            doSearch();
        });

        $("#btnReturnToMap").click(function(e) {
            e.preventDefault();
            window.location = "${createLink(controller:'map', action:'index')}";
        });


        $("#btnStartOver").click(function(e) {
            e.preventDefault();
            window.location = "${createLink(controller:'search', action:'clearCurrentSearch')}";
        });

        $("#searchText").keydown(function (e) {
            if (e.keyCode == 13) {
                e.preventDefault();
                doSearch();
            }
        });

        $("#useBoundingBox").change(function (e) {
            e.preventDefault();
            if ($(this).prop("checked")) {
                $("#mapDiv").css("display", "block");
                if (!map) {
                    showMap();
                }
                map.updateSize();
            } else {
                $("#mapDiv").css("display", "none");
            }

        });

        <g:if test="${userSearch?.useBoundingBox}">
        $("#mapDiv").css("display", "block");
        $("#useBoundingBox").prop("checked", true);
        showMap();
        </g:if>

        $("#btnClearCurrentSelection").click(function (e) {
            e.preventDefault();
            $.ajax("${createLink(controller:'studyLocation', action:'clearSelectedStudyLocationVisits')}").done(function () {
                renderSelectedList();
            });
        });

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

        $("#btnAddCriteria").click(function (e) {
            e.preventDefault();
            showModal({
                url: "${createLink(action:'ajaxAddSearchCriteriaFragment')}",
                title: "Add Search Criteria",
                height: 520,
                width: 700,
                onClose: function () {
                    renderCriteria();
                }
            });
        });

        $("#btnRemoveAllCriteria").click(function (e) {
            e.preventDefault();
            $.ajax("${createLink(action:'ajaxDeleteAllSearchCriteria', params:[userSearchId: appState.currentSearch?.id])}").done(function (e) {
                renderCriteria();
            });

        });

        renderSelectedList();
        renderCriteria();

    });

    function renderSelectedList() {
        $.ajax("${createLink(controller:'studyLocation', action:'ajaxSelectedStudyLocationsFragment')}").done(function (content) {
            $("#selectedStudyLocations").html(content);
            // Hook up button event handlers...
            $(".btnRemoveSelectedPlot").click(function (e) {
                e.preventDefault();
                var studyLocationVisitId = $(this).parents("[studyLocationVisitId]").attr("studyLocationVisitId");
                if (studyLocationVisitId) {
                    deselectVisit(studyLocationVisitId);
                }
            });
        });

        if (map) {
            refreshStudyLocationPoints();
        }
    }

    function selectVisit(studyLocationName, studyLocationVisitId, successCallback) {
        $.ajax("${createLink(controller: 'studyLocation', action:'selectStudyLocationVisit')}?studyLocationName=" + studyLocationName + "&studyLocationVisitId=" + studyLocationVisitId).done(function () {

            renderSelectedList();

            if (successCallback) {
                successCallback();
            }
        });
    }

    function deselectVisit(studyLocationVisitId, successCallback) {
        $.ajax("${createLink(controller:'studyLocation', action:'deselectStudyLocationVisit')}?studyLocationVisitId=" + studyLocationVisitId).done(function (data) {

            renderSelectedList();

            if (successCallback) {
                successCallback();
            }

            if (map) {
                refreshStudyLocationPoints();
            }
        });
    }


    function selectPlot(studyLocationName, successCallback) {
        $.ajax("${createLink(controller:'studyLocation', action:'selectStudyLocation')}?studyLocationName=" + studyLocationName).done(function (data) {
            if (successCallback) {
                successCallback();
            }
        });
    }

    function deselectPlot(studyLocationName, successCallback) {
        $.ajax("${createLink(controller:'studyLocation', action:'deselectStudyLocation')}?studyLocationName=" + studyLocationName).done(function (data) {
            if (successCallback) {
                successCallback();
            }
        });
    }

    function selectPlots(studyLocationNames, successCallback) {
        var studyLocationstring = studyLocationNames.join(",");
        $.ajax("${createLink(controller:'studyLocation', action:'selectStudyLocations')}?studyLocationNames=" + studyLocationstring).done(function (data) {
            if (successCallback) {
                successCallback();
            }
        });
    }

    function selectVisits(visits, successCallback) {
        var idString = visits.join(",");
        $.ajax("${createLink(controller:'studyLocation', action:'selectStudyLocationVisits')}?studyLocationVisitIds=" + idString).done(function (data) {
            if (successCallback) {
                successCallback();
            }
        });
    }

    function initMap() {

        var options = {
            projection: "EPSG:900913",
            maxExtent: new OpenLayers.Bounds(-20037508, -20037508, 20037508, 20037508),
            units: "m",
            controls: [
                new OpenLayers.Control.Navigation({ mouseWheelOptions: {interval: 50, cumulative: false}, zoomBoxEnabled: true }),
                new OpenLayers.Control.ArgParser(),
                new OpenLayers.Control.PanZoom(),
                new OpenLayers.Control.PanZoomBar()
            ]
        };

        map = new OpenLayers.Map('mapContent', options);

        map.addControl(new OpenLayers.Control.LayerSwitcher());

        var gphy = new OpenLayers.Layer.Google(
            "Google Physical",
            {type: google.maps.MapTypeId.TERRAIN}
        );
        var gmap = new OpenLayers.Layer.Google(
            "Google Streets", // the default
            {numZoomLevels: 20}
        );
        var ghyb = new OpenLayers.Layer.Google(
            "Google Hybrid",
            {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20}
        );
        var gsat = new OpenLayers.Layer.Google(
            "Google Satellite",
            {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 20}
        );

        map.addLayers([gphy, gmap, ghyb, gsat]);

        <g:if test="${userSearch?.useBoundingBox}">
        var extent = new OpenLayers.Bounds(
            ${userSearch.left},
            ${userSearch.bottom},
            ${userSearch.right},
            ${userSearch.top}
        );

        var projWGS84 = new OpenLayers.Projection("EPSG:4326");
        var proj900913 = new OpenLayers.Projection("EPSG:900913");
        extent.transform(projWGS84, proj900913);

        map.zoomToExtent(extent, true);
        </g:if>
        <g:else>
        var latLongProj = new OpenLayers.Projection("EPSG:4326");
        var point = new OpenLayers.LonLat(133, -28);
        point.transform(latLongProj, map.getProjectionObject());
        map.setCenter(point, 5);
        </g:else>

        map.events.register("moveend", null, onMapMoved);
        map.events.register("zoomend", null, onMapMoved);

        syncMapExtents();

    }

    function syncMapExtents() {

        var projWGS84 = new OpenLayers.Projection("EPSG:4326");
        var proj900913 = new OpenLayers.Projection("EPSG:900913");
        var extent = map.getExtent().transform(proj900913, projWGS84);


        $("#top").val(extent.top);
        $("#spanTop").html(extent.top);

        $("#left").val(extent.left);
        $("#spanLeft").html(extent.left);

        $("#bottom").val(extent.bottom);
        $("#spanBottom").html(extent.bottom);

        $("#spanRight").html(extent.right);
        $("#right").val(extent.right);

    }

    function onMapMoved() {
        syncMapExtents();
    }

    function addPlotPointsLayer(studyLocationList) {

        var studyLocations = map.getLayer("Plots");
        if (studyLocations) {
            map.removeLayer(studyLocations);
        }

        var latLongProj = new OpenLayers.Projection("EPSG:4326");
        studyLocations = new OpenLayers.Layer.Markers("Plots");
        var results = studyLocationList;

        var size = new OpenLayers.Size(32, 32);
        var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
        var icon = new OpenLayers.Icon('${resource(dir:'/images', file:'s2s-marker.png')}', size, offset);
        var selectedIcon = new OpenLayers.Icon('${resource(dir:'/images', file:'s2s-marker-selected.png')}', size, offset);

        for (resultKey in results) {

            var result = results[resultKey];
            var location = new OpenLayers.LonLat(parseFloat(result.longitude), parseFloat(result.latitude));

            location.transform(latLongProj, map.getProjectionObject());

            var pinIcon = null;

            if (result.selected) {
                pinIcon = selectedIcon.clone();
            } else {
                pinIcon = icon.clone();
            }

            var marker = new OpenLayers.Marker(location, pinIcon);

            studyLocations.addMarker(marker);
            marker.tag = result.siteName;

            marker.events.register('mouseover', marker, function (e) {
                //showPlotHover(this.tag);
            });

            marker.events.register('mouseout', marker, function (e) {
                //hidePlotHover(this.tag);
            });

            marker.events.register('click', marker, function (e) {
                //showPlotDetails(this.tag);
            });

        }
        studyLocations.id = "Plots";

        map.addLayer(studyLocations);

    }


    function refreshStudyLocationPoints() {
        $.ajax("${createLink(controller: 'studyLocation', action: 'getUserDisplayedPoints')}").done(function (data) {
            addPlotPointsLayer(data);
        });
    }

    function renderCriteria() {
        $.ajax("${createLink(action: 'ajaxCriteriaListFragment', params:[userSearchId: userInstance.applicationState.currentSearch?.id])}").done(function (content) {
            $("#searchCriteria").html(content);
        });
    }



</script>

<div class="container-fluid">
    <legend>
        <table style="width:100%">
            <tr>
                <td>
                    <sts:homeBreadCrumb/>
                    <sts:navSeperator/>
                    <span class="sts-breadcrumb">Find Study Location Visits</span>
                </td>
            </tr>
        </table>
    </legend>

    <div class="row-fluid">
        <div class="span8">
            <div class="well well-small">

                <form class="form-horizontal" id="searchForm">

                    <div class="control-group">
                        <label class="control-label" for='searchText'>Full or partial site name</label>

                        <div class="controls">
                            <g:textField class="input-xlarge" id="searchText" name="searchText" placeholder="Search" value="${userSearch?.searchText}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for='useBoundingBox'>Use bounding box</label>

                        <div class="controls">
                            <g:checkBox class="input-xlarge" id="useBoundingBox" name="useBoundingBox" value="${userSearch?.useBoundingBox}"/>
                        </div>
                    </div>

                    <div id="mapDiv" style="display: none; margin-bottom: 5px">
                        <table>
                            <tr>
                                <td>
                                    <div id="mapContainer">
                                        <div id="mapContent" style="height: 400px; width: 400px"></div>
                                    </div>
                                </td>
                                <td style="vertical-align: bottom">

                                    <g:hiddenField class="input-small" name="top" value="${userSearch?.top}"/>
                                    <g:hiddenField class="input-small" name="left" value="${userSearch?.left}"/>
                                    <g:hiddenField class="input-small" name="bottom" value="${userSearch?.bottom}"/>
                                    <g:hiddenField class="input-small" name="right" value="${userSearch?.right}"/>

                                    <small style="color: #a9a9a9">
                                        <table>
                                            <tr>
                                                <td>Top</td>
                                                <td><span id="spanTop"></span></td>
                                            </tr>
                                            <tr>
                                                <td>Left</td>
                                                <td><span id="spanLeft"></span></td>
                                            </tr>
                                            <tr>
                                                <td>Bottom</td>
                                                <td><span id="spanBottom"></span></td>
                                            </tr>
                                            <tr>
                                                <td>Right</td>
                                                <td><span id="spanRight"></span></td>
                                            </tr>
                                        </table>
                                    </small>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <div id="searchCriteria"></div>

                    <button type="button" id="btnAddCriteria" class="btn btn-small btn-info"><i class="icon-plus icon-white"></i>&nbsp;Add Criteria</button>
                    <button type="button" id="btnRemoveAllCriteria" class="btn btn-small btn-warning"><i class="icon-remove icon-white"></i>&nbsp;Clear Criteria</button>

                    <button type="button" id="btnSearch" class="btn btn-primary pull-right">
                        <i class="icon-search icon-white"></i>&nbsp;Search
                    </button>
                    <button type="button" id="btnStartOver" class="btn pull-right" style="margin-right: 5px">
                        <i class="icon-remove-circle"></i>&nbsp;Start over
                    </button>
                    <button type="button" id="btnReturnToMap" class="btn pull-right" style="margin-right: 5px">
                        <i class="icon-home"></i>&nbsp;Return to Map
                    </button>

                </form>
            </div>

            <div id="searchResultsDiv"></div>

        </div>

        <div id="sidebarContainer" class="span4">
            <div id="sidebarContent" class="well well-small">

                <h5>Selected Study Location Visits</h5>

                <div id="selectedStudyLocations">
                    <sts:loading message="Loading selected visits..." />
                </div>

                <div>
                    <button id="btnClearCurrentSelection" class="btn btn-small" style="margin-right:5px">Clear all selected</button>
                </div>
            </div>
        </div>

    </div>
</div>
</body>
</html>