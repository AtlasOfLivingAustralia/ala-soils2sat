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

<style type="text/css">

    td.toolButtonCell {
        width: 20px;
        padding: 4px 2px;
    }

</style>

<div class="well well-small">

    <ul class="nav nav-tabs" style="margin-bottom: 0px">
        <li class="active"><a href="#selectionContextTabPane" data-toggle="tab" class="sidebarTab" id="selectionTab">Selection</a></li>
        <li><a href="#mapLayersTabPane" data-toggle="tab" class="sidebarTab" id="mapLayersTab">Map Layers</a></li>
    </ul>

    <div class="tab-content">

        <div class="tab-pane active" id="selectionContextTabPane">
            <h5>Selected Study Location Visits
                <g:if test="${appState?.selectedVisits}">
                    (${appState.selectedVisits?.size()})
                </g:if>
            </h5>
            <table style="width: 100%; margin-bottom: 10px">
                <tr>
                    <td>
                        <button class="btn btn-small btn-info" id="btnFindStudyLocationVisits">Search&nbsp;<i class="icon-search icon-white"></i></button>
                    </td>
                    <td style="text-align: right">
                        <button id="btnToggleShowSelected" class="btn btn-small ${appState.plotOnlySelectedLocations ? 'active' : ''}">Show selected only</button>
                    </td>
                </tr>
            </table>

            <div id="selectedContent" style="overflow-y: auto">
                <g:render template="/map/selectionTable" model="${[appState: appState]}]" />
            </div>

            <p style="">
                <button id="btnCompare" class="btn btn-small btn-primary">Compare</button>
                <button class="btn btn-small btnExtract">Extract Data</button>
                <button class="btn btn-small" id="btnClearSelectedVisits">Remove All</button>
            </p>

        </div>

        <div class="tab-pane" id="mapLayersTabPane">
            <p style="margin-bottom: 10px;">
                <button id="btnLayerAdd" class="btn btn-mini btn-success"><i class="icon-plus icon-white"></i>&nbsp;Add Map Layer</button>
                <button id="btnLayerRemoveAll" class="btn btn-mini btn-danger"><i class="icon-remove icon-white"></i>&nbsp;Remove All Layers</button>
            </p>

            <div id="layersTable" style="overflow-y: auto">
                <g:if test="${appState?.layers}">
                    <table class="table table-striped table-condensed">
                        <g:each in="${appState?.layers}">
                            <tr>
                              <td><small><sts:layerDisplayName class="showLayerInfoLink" href="#" layerName="${it.name}"/></small></td>
                            <td class="toolButtonCell">
                                <button class="btn btn-mini btnToggleLayerVisibility ${it.visible ? 'btn-info' : ''}" layerName="${it.name}" title="Show/Hide this layer in the map"><i class="icon-eye-open"/>
                                </button></td>
                            </td>
                            <td class="toolButtonCell">
                                <button class="btn btn-mini btnLayerTools" style="margin-right:5px" title="Toggle layer display settings" layerName="${it.name}"><i class="icon-wrench"/>
                                </button>
                            </td>

                            <td class="toolButtonCell">
                                <button class="btn btn-mini btnLayerInfo" style="margin-right:5px" title="Display layer information" layerName="${it.name}"><i class="icon-info-sign"/>
                                </button>
                            </td>
                            <td class="toolButtonCell">
                                <button class="btn btn-mini btnRemoveLayer" layerName="${it.name}" title="Remove layer from the list"><i class="icon-remove"/>
                                </button>
                            </td>
                            </tr>
                            <tr style="display:none" id="layerTools_${it.name}" class="layerToolsRow">
                                <td colspan="5" class="layerToolsCell"></td>
                            </tr>
                        </g:each>
                    </table>
                </g:if>
                <g:else>
                    No layers have been added
                </g:else>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $("#btnTogglePlotSelected").click(function (e) {
        e.preventDefault();
        var currentState = $(this).hasClass('active');
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        } else {
            $(this).addClass('active');
        }

        $.ajax("${createLink(controller: 'studyLocation', action: 'ajaxSetStudyLocationSelectedOnly')}?plotSelected=" + !currentState).done(function (e) {
            refreshStudyLocationPoints();
        });

    });

    $("#btnToggleShowSelected").click(function (e) {
        e.preventDefault();
        var currentState = $(this).hasClass('active');
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        } else {
            $(this).addClass('active');
        }

        $.ajax("${createLink(controller: 'studyLocation', action: 'ajaxSetStudyLocationSelectedOnly')}?plotSelected=" + !currentState + "&mode=visits").done(function (e) {
            refreshStudyLocationPoints();
        });

    });


    $("#btnQuestions").click(function (e) {
        e.preventDefault();
        window.open("${createLink(controller:'question')}", "Questions");
    });

    $(".btnExtract").click(function (e) {
        e.preventDefault();
        window.location = "${createLink(controller:'extract')}";
    });


    $("#btnCompare").click(function (e) {
        e.preventDefault();
        compareSelected();
    });

    $(".studyLocationDetailsLink").mouseover(function (e) {
        e.preventDefault();
        var studyLocationName = $(this).parents('[studyLocationName]').attr("studyLocationName");
        showPlotHover(studyLocationName);
    });

    $(".studyLocationDetailsLink").mouseout(function (e) {
        e.preventDefault();
        var studyLocationName = $(this).parents('[studyLocationName]').attr("studyLocationName");
        hidePlotHover(studyLocationName);
    });

    $(".btnLayerInfo").click(function (e) {
        var layerName = $(this).attr("layerName");
        displayLayerInfo(layerName);
    });

    $("#btnClearSelectedPlots").click(function (e) {
        e.preventDefault();
        clearSelectedPlots();
    });

    $("#btnClearSelectedVisits").click(function (e) {
        e.preventDefault();
        clearSelectedVisits();
    });

    $("#btnFindPlot").click(function (e) {
        e.preventDefault();
        findStudyLocations();
    });

    $("#btnFindStudyLocationVisits").click(function(e) {
        e.preventDefault();
        findStudyLocationVisits();
    });

    $("#btnLayerAdd").click(function (e) {
        e.preventDefault();
        addLayerClicked();
    });

    $("#btnLayerRemoveAll").click(function (e) {
        e.preventDefault();
        clearAllLayers();
    });

    $(".btnRemoveLayer").click(function (e) {
        e.preventDefault();
        var layerName = $(this).attr("layerName");
        removeLayer(layerName);
    });

    $(".btnToggleLayerVisibility").click(function (e) {
        var element = $(this);
        var layerName = element.attr("layerName");
        if (layerName) {
            if ($(this).hasClass("btn-info")) {
                $.ajax("${createLink(controller: 'map', action: 'ajaxSetLayerVisibility')}?layerName=" + layerName + '&visibility=false').done(function (e) {
                    unloadWMSLayer(layerName);
                    element.removeClass("btn-info");
                });
            } else {
                $.ajax("${createLink(controller: 'map', action: 'ajaxSetLayerVisibility')}?layerName=" + layerName + '&visibility=true').done(function (e) {
                    loadWMSLayer(layerName, e.opacity, e.style);
                    element.addClass("btn-info");
                });
            }
        }
    });

    $(".btnLayerTools").click(function (e) {
        var element = $(this);
        var layerName = element.attr("layerName");

        var isCurrentActive = $(this).hasClass("btn-warning");

        $(".btnLayerTools").removeClass("btn-warning");
        $(".layerToolsRow").hide();
        $(".layerToolsCell").html("");

        if (layerName) {
            if (!isCurrentActive) {
                element.addClass("btn-warning");
                $("#layerTools_" + layerName).show();
                $.ajax("${createLink(controller: 'map', action: 'layerToolsFragment')}?layerName=" + layerName).done(function (content) {
                    $("#layerTools_" + layerName + " > td").html(content);
                });
            }
        }

    });

    function resizeTabPanes() {
        var mapHeight = $("#mapContent").height() + $("#mapContent").offset().top;
        var height = mapHeight - 140;
        $(".tab-pane").height(height);
        $("#selectedContent").height(height - 90);
        $("#layersTable").height(height - 60);
    }

    $(window).resize(function (e) {
        resizeTabPanes();
    });

    $(".showLayerInfoLink").click(function (e) {
        e.preventDefault();
        var layerName = $(this).attr("layerName");
        displayLayerInfo(layerName);
    });

    var selectedTab = $('#${appState.sidebarSelectedTab}');
    if (selectedTab) {
        selectedTab.click();
    }

    resizeTabPanes();

    $(".sidebarTab").click(function(e) {

        var selectedTab = $(this).attr("id");

        if (selectedTab) {
            $.ajax("${createLink(contoller:'map', action:'ajaxSetSelectedSidebarTab')}?selectedTab=" + selectedTab).done(function(e) {
                // refreshStudyLocationPoints();
            });
        }

    });

</script>
