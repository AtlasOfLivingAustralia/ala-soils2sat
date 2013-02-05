<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
<head>
    <r:require module='jqueryui'/>
    <r:require module='bootstrap_responsive'/>
    <r:require module='openlayers'/>

    <meta name="layout" content="detail"/>
    <title>Find Study Locations</title>\
</head>

<body>

<script src="http://maps.google.com/maps/api/js?key=AIzaSyDAqE0sOMHNienA0zFIUMlY53ztEDiv0d8&sensor=true" type="text/javascript"></script>
<link rel="stylesheet" href="${resource(dir: 'js/openlayers/theme/default', file: 'style.css')}" type="text/css">


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
        $("#btnSearch").click();
    }

    $(document).ready(function () {

        $("#searchText").keydown(function(e) {
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

        <g:if test="${params.useBoundingBox}">
            $("#mapDiv").css("display", "block");
            $("#useBoundingBox").prop("checked", true);
            showMap();
        </g:if>

        $("#btnClearCurrentSelection").click(function (e) {
            e.preventDefault();
            $.ajax("${createLink(controller:'studyLocation', action:'clearSelectedStudyLocations')}").done(function () {
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
                title: "Coming Soon!",
                height: 520,
                width: 700
            });

        });

        renderSelectedList();

    });

    function renderSelectedList() {
        $.ajax("${createLink(controller:'studyLocation', action:'ajaxSelectedStudyLocationsFragment')}").done(function (content) {
            $("#selectedStudyLocations").html(content);
            // Hook up button event handlers...
            $(".btnRemoveSelectedPlot").click(function (e) {
                e.preventDefault();
                var studyLocationName = $(this).attr("studyLocationName");
                deselectPlot(studyLocationName, function () {
                    renderSelectedList();
                });
            });
        });
        if (map) {
            refreshStudyLocationPoints();
        }
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

        <g:if test="${params.top && params.left && params.bottom && params.right}">
            var extent = new OpenLayers.Bounds(
                ${params.left},
                ${params.bottom},
                ${params.right},
                ${params.top}
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
        var extent = map.getExtent().transform(proj900913, projWGS84 );


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
        var offset = new OpenLayers.Pixel(-(size.w / 2), 0);
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
        $.ajax("${createLink(controller: 'studyLocation', action: 'getUserDisplayedPlots')}").done(function (data) {
            addPlotPointsLayer(data);
        });
    }



</script>

<div class="container-fluid">
    <legend>
        <table style="width:100%">
            <tr>
                <td><a href="${createLink(controller: 'map', action: 'index')}">Map</a>&nbsp;&#187;&nbsp;Find Study Locations
                </td>
            </tr>
        </table>
    </legend>

    <div class="row-fluid">
        <div class="span8">
            <div class="well well-small">

                <g:form class="form-horizontal" controller="studyLocation" action="findStudyLocations" autocomplete="off" id="searchForm" >

                    <div class="control-group">
                        <label class="control-label" for='searchText'>Full or partial site name</label>

                        <div class="controls">
                            <g:textField class="input-xlarge" id="searchText" name="searchText" placeholder="Search" value="${params.searchText}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for='useBoundingBox'>Use bounding box</label>

                        <div class="controls">
                            <g:checkBox class="input-xlarge" id="useBoundingBox" name="useBoundingBox" value="${params.useBoundingBox}"/>
                        </div>
                    </div>

                    <div id="mapDiv" style="display: none; margin-bottom: 5px">
                        <table>
                            <tr>
                                <td>
                                    <div id="mapContainer">
                                        <div id="mapContent" style="height: 400px; width: 400px">
                                        </div>
                                    </div>

                                </td>
                                <td style="vertical-align: bottom">

                                    <g:hiddenField class="input-small" name="top" />
                                    <g:hiddenField class="input-small" name="left"/>
                                    <g:hiddenField class="input-small" name="bottom"/>
                                    <g:hiddenField class="input-small" name="right"/>
                                    <small style="color: #a9a9a9">
                                        <table>
                                            <tr>
                                                <td>Top</td>
                                                <td><span id="spanTop" ></span></td>
                                            </tr>
                                            <tr>
                                                <td>Left</td>
                                                <td><span id="spanLeft" ></span></td>
                                            </tr>
                                            <tr>
                                                <td>Bottom</td>
                                                <td><span id="spanBottom" ></span></td>
                                            </tr>
                                            <tr>
                                                <td>Right</td>
                                                <td><span id="spanRight" ></span></td>
                                            </tr>
                                        </table>
                                    </small>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <button id="btnAddCriteria" class="btn btn-small btn-info"><i class="icon-plus icon-white"></i>&nbsp;Add search criteria
                    </button>

                    <button type="submit" id="btnSearch" class="btn btn-primary pull-right">Search</button>
                </g:form>
            </div>

            <g:if test="${searchPerformed}">
                <div class="well well-small">
                    <h3>Search Results</h3>
                    <g:if test="${!results}">
                        Your search returned no results.
                    </g:if>
                    <g:else>
                        <div>
                            ${results.size()} Study Locations found
                        </div>

                        <div class="well well-small" style="height: 245px; overflow-y: scroll;">
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

                    %{--<div>--}%
                    %{--<button id="btnSelectAllSearchResults" class="btn btn-small btn-info pull-right">Select all</button>--}%
                    %{--</div>--}%
                    </g:else>
                </div>
            </g:if>
        </div>

        <div id="sidebarContainer" class="span4">
            <div id="sidebarContent" class="well well-small">
                <h5>Selected Study Locations</h5>

                <div id="selectedStudyLocations">
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