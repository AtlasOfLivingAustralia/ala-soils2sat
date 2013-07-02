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

<%@ page import="au.org.ala.soils2sat.LayerStyle" %>
<!doctype html>
<html>
    <head>
        <r:require module='openlayers'/>
        <meta name="layout" content="soils2sat"/>
        <title>Soils to Satellites</title>
        <style type="text/css" media="screen">

        #content {
            top: 62px;
            bottom: 60px;
            position: absolute;
            width: 100%;
        }

        #mapContent {
            height: 630px;
        }

        #layersContent {
            float: right;
            display: inline-block;
            width: 200px;
        }

        .info-popup {
            position: absolute;
            bottom: 100px;
            right: 10px;
            width: 350px;
            height: 200px;
            background: #393939;
            border: 3px solid #393939;
            color: white;
            border-radius: 10px;
            padding: 10px;
            z-index: 9999;
        }

        .info-popup h4 {
            margin: 2px;
        }

        .olLayerGooglePoweredBy {
            display: none;
        }

        </style>

        <script src="http://maps.google.com/maps/api/js?key=AIzaSyDAqE0sOMHNienA0zFIUMlY53ztEDiv0d8&sensor=true" type="text/javascript"></script>
        <link rel="stylesheet" href="${resource(dir: 'js/openlayers/theme/default', file: 'style.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: '/jqwidgets/styles', file: 'jqx.base.css')}" type="text/css"/>

    </head>

    <body>

        <script type="text/javascript">

            var map;

            $(window).resize(function (e) {
                resizeMap();
            });

            function resizeMap() {
                var height = $(window).height() - (63 + 70);
                $("#mapContent").height(height);
                if (map) {
                    setTimeout(function () {
                        map.updateSize();
                    }, 200);
                }

            }

            function refreshSidebar() {
                $.ajax('${createLink(controller:'map', action:'sideBarFragment')}').done(function (html) {
                    $("#sidebarContent").html(html);
                });
            }

            function selectVisit(studyLocationName, studyLocationVisitId, successCallback) {
                $.ajax("${createLink(controller: 'studyLocation', action:'selectStudyLocationVisit')}?studyLocationName=" + studyLocationName + "&studyLocationVisitId=" + visitId).done(function () {
                    refreshSidebar();
                    if (successCallback) {
                        successCallback();
                    }
                });
            }

            function deselectVisit(studyLocationVisitId, successCallback) {
                $.ajax("${createLink(controller:'studyLocation', action:'deselectStudyLocationVisit')}?studyLocationVisitId=" + studyLocationVisitId).done(function (data) {
                    refreshSidebar();
                    refreshStudyLocationPoints();
                    if (successCallback) {
                        successCallback();
                    }
                });
            }

            function clearSelectedVisits(successCallback) {
                $.ajax("${createLink(controller:'studyLocation', action:'clearSelectedStudyLocationVisits')}").done(function (data) {
                    refreshSidebar();
                    refreshStudyLocationPoints();
                    if (successCallback) {
                        successCallback();
                    }
                });
            }

            function findStudyLocations() {
                window.location = "${createLink(controller:'search', action:'findStudyLocations')}";
                return true;
            }

            function findStudyLocationVisits() {
                window.location = "${createLink(controller:'search', action:'findStudyLocationVisits')}";
                return true;
            }

            function compareSelected() {
                window.location = "${createLink(controller: 'studyLocation', action:'compareStudyLocations')}";
            }

            function addLayerClicked() {
                showModal({
                    url: "${createLink(controller: 'map', action:'addLayerFragment')}",
                    title: "Add Map Layer",
                    height: 560,
                    width: 700
                });
                return true;
            }

            function unloadWMSLayer(layerName) {
                var candidates = map.getLayersByName(layerName);
                if (candidates) {
                    for (var i in candidates) {
                        var layer = candidates[i];
                        layer.destroy();
                    }
                }
            }

            function removeLayer(layerName) {
                $.ajax("${createLink(controller: 'map', action:'removeLayer')}?layerName=" + layerName).done(function (e) {
                    unloadWMSLayer(layerName);
                    refreshSidebar();
                });
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

                var visitIcon = new OpenLayers.Icon('${resource(dir:'/images', file:'s2s-visit-marker.png')}', size, offset);
                var visitSelectedIcon = new OpenLayers.Icon('${resource(dir:'/images', file:'s2s-visit-marker-selected.png')}', size, offset);


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
                    marker.tag = result.studyLocationName;

                    marker.events.register('mouseover', marker, function (e) {
                        showPlotHover(this.tag);
                    });

                    marker.events.register('mouseout', marker, function (e) {
                        hidePlotHover(this.tag);
                    });

                    marker.events.register('click', marker, function (e) {
                        showPlotDetails(this.tag);
                    });

                }
                studyLocations.id = "Plots";

                map.addLayer(studyLocations);
                hideMessagePanel();

            }

            function refreshStudyLocationPoints() {
                $.ajax("${createLink(controller: 'studyLocation', action: 'getUserDisplayedPoints')}").done(function (data) {
                    addPlotPointsLayer(data);
                });
            }

            $(document).ready(function (e) {

                $("body").css("overflow", "hidden");

                resizeMap();
                initMap();
                refreshSidebar();
                refreshStudyLocationPoints();

                $("#btnToggleSidebar").click(function (e) {
                    toggleSidebar();
                });

                <g:each in="${appState?.layers}" var="layer">
                    <g:if test="${layer.visible}">
                        <g:set var="style" value="${LayerStyle.findByLayerName(layer.name)?.style ?: ''}"/>
                        loadWMSLayer("${layer.name}", ${layer.opacity ?: 1.0}, '${style}');
                    </g:if>
                </g:each>
            });

            function addLayer(name, addToMap) {
                var showInMap = false;
                if (addToMap) {
                    showInMap = true;
                }
                $.ajax("${createLink(controller: 'map', action: 'addLayer')}?layerName=" + name + "&addToMap=" + showInMap).done(function (data) {
                    refreshSidebar();
                    if (showInMap) {
                        loadWMSLayer(name, 1.0, data.style);
                    }
                });

            }

            function addLayerSet(layerSetId, replaceExisting) {
                $.ajax("${createLink(controller: 'map', action: 'addLayerSet')}?layerSetId=" + layerSetId + "&replaceExisting=" + replaceExisting).done(function (data) {
                    refreshSidebar();
                });
            }

            function clearAllLayers() {
                $.ajax("${createLink(controller: 'map', action: 'removeAllLayers')}").done(function (data) {
                    refreshSidebar();
                });
            }

            function loadWMSLayer(name, opacity, styles) {

                if (!styles) {
                    styles = '';
                }

                var url = "${grailsApplication.config.spatialPortalRoot}/geoserver/gwc/service/wms/reflect";

                if (styles.length > 0) {
                    // bypass the tile cache when styles are in effect
                    url = "${grailsApplication.config.spatialPortalRoot}/geoserver/wms/reflect"
                }

                var wmsLayer = new OpenLayers.Layer.WMS(name, url, {
                    layers: 'ALA:' + name,
                    srs: 'EPSG:900913',
                    format: 'image/png',
                    transparent: true,
                    styles: styles
                });

                if (opacity) {
                    wmsLayer.setOpacity(opacity);
                }

                // wmsLayer.visibility = false;
                map.addLayer(wmsLayer);
                var studyLocations = map.getLayer("Plots");
                if (studyLocations) {
                    map.removeLayer(studyLocations);
                    map.addLayer(studyLocations);
                }
            }

            function initMap() {

                infoMessage("Initialising map...");

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

                var latLongProj = new OpenLayers.Projection("EPSG:4326");

                <g:if test="${appState?.viewExtent}">
                var extent = new OpenLayers.Bounds(
                    ${appState.viewExtent?.left},
                    ${appState.viewExtent?.bottom},
                    ${appState.viewExtent?.right},
                    ${appState.viewExtent?.top}
                );
                map.zoomToExtent(extent, true);
                </g:if>
                <g:else>
                var point = new OpenLayers.LonLat(133, -28);
                point.transform(latLongProj, map.getProjectionObject());
                map.setCenter(point, 5);
                </g:else>

                map.events.register("moveend", null, onMapMoved);
                map.events.register("zoomend", null, onMapMoved);

            }

            function showPlotDetails(studyLocationName) {
                if (studyLocationName) {
                    window.location = "${createLink(controller: 'studyLocation', action:'studyLocationSummary', )}?studyLocationName=" + studyLocationName;
                    return true;
                }
                return false;
            }

            function showVisitDetails(studyLocationName, studyLocationVisitId) {
                if (studyLocationVisitId && studyLocationName) {
                    window.location = "${createLink(controller: 'studyLocation', action:'studyLocationVisitSummary', )}?studyLocationVisitId=" + studyLocationVisitId + "&studyLocationName=" + studyLocationName;
                    return true;
                }
                return false;
            }

            var studyLocationHoverFlag = false;

            function showPlotHover(studyLocationName) {
                studyLocationHoverFlag = true;
                var url = "${createLink(controller:'map', action:'ajaxPlotHover')}?studyLocationName=" + studyLocationName;
                $.ajax(url).done(function (html) {
                    if (studyLocationHoverFlag) {
                        $("#studyLocationSummary").css("display", "block").html(html);
                    }
                });

            }

            function setLayerOpacity(layerName, opacity) {
                var candidates = map.getLayersByName(layerName);

                if (candidates) {
                    $.ajax("${createLink(controller: 'map', action:'ajaxSaveLayerOpacity')}?layerName=" + layerName + "&opacity=" + opacity).done(function (result) {
                        for (var i in candidates) {
                            var layer = candidates[i];
                            layer.setOpacity(opacity);
                        }
                    });
                }

            }

            function hidePlotHover(studyLocationName) {
                studyLocationHoverFlag = false;
                hideMessagePanel();
            }

            function hideMessagePanel() {
                $('#studyLocationSummary').css("display", "none");
            }

            function infoMessage(message) {
                var html = "<h4>" + message + "</h4>";
                $("#studyLocationSummary").css("display", "block").html(html);
            }

            function toggleSidebar() {
                if ($('#mapContainer').hasClass("span8")) {
                    $('#mapContainer').removeClass("span8").addClass("span12");
                    $('#sidebarContainer').removeClass("span4").addClass("span0");
                    $("#btnToggleSidebar").html("Show sidebar");
                } else {
                    $('#mapContainer').removeClass("span12").addClass("span8");
                    $('#sidebarContainer').removeClass("span0").addClass("span4");
                    $("#btnToggleSidebar").html("Hide sidebar");
                }
                resizeMap();
            }

            function onMapMoved(e) {
                if (map) {
                    var extent = map.getExtent();
                    var url = "${createLink(controller: 'map', action:'ajaxSaveCurrentExtent')}?top=" + extent.top + "&left=" + extent.left + "&bottom=" + extent.bottom + "&right=" + extent.right;
                    $.ajax(url).done(function (e) {
                    });
                }
            }

        </script>

        <content tag="topLevelNav">home</content>

        <div class="container-fluid">
            <div class="row-fluid">
                <div id="mapContainer" class="span8">
                    <div id="mapContent">
                    </div>
                </div>

                <div id="sidebarContainer" class="span4">
                    <div id="sidebarContent">
                        <sts:loading message="Loading..."/>
                    </div>
                </div>
            </div>
        </div>

        <div class="info-popup" id="studyLocationSummary" style="display: none; ">
            <H2>Plot details</H2>
        </div>

        <content tag="buttonBar">
            <button id="btnToggleSidebar" class="btn btn-small">Hide sidebar</button>
        </content>

        <script src="${resource(dir: '/jqwidgets', file: 'jqxcore.js')}" type="text/javascript"></script>
        <script src="${resource(dir: '/jqwidgets', file: 'jqxpanel.js')}" type="text/javascript"></script>
        <script src="${resource(dir: '/jqwidgets', file: 'jqxscrollbar.js')}" type="text/javascript"></script>
        <script src="${resource(dir: '/jqwidgets', file: 'jqxbuttons.js')}" type="text/javascript"></script>
        <script src="${resource(dir: '/jqwidgets', file: 'jqxexpander.js')}" type="text/javascript"></script>
        <script src="${resource(dir: '/jqwidgets', file: 'jqxtree.js')}" type="text/javascript"></script>

    </body>
</html>
