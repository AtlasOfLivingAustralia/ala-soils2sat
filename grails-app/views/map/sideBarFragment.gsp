<%@ page import="au.org.ala.soils2sat.MapSelectionMode" %>
<style type="text/css">

td.toolButtonCell {
    width: 20px;
    padding: 4px 2px;
}

</style>

<div class="well well-small">

    <ul class="nav nav-tabs" style="margin-bottom: 0px">
        <li class="active"><a href="#studyLocationsTab" data-toggle="tab" class="mapSelectModeTab" mapSelectionMode="StudyLocation">Study Locations</a></li>
        <li><a href="#studyLocationVisitsTab" data-toggle="tab" id="" class="mapSelectModeTab" mapSelectionMode="StudyLocationVisit">Study Location Visits</a></li>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="studyLocationsTab">
            <table style="width: 100%">
                <tr>
                    <td>
                        <h5>Selected Study Locations
                        <g:if test="${appState?.selectedPlots}">
                            (${appState.selectedPlots?.size()})
                        </g:if>
                        </h5>
                    </td>
                    <td style="text-align: right">
                        <button id="btnTogglePlotSelected" class="btn btn-mini ${appState.plotOnlySelectedLocations ? 'active' : ''}">Show selected only</button>
                    </td>
                </tr>
            </table>

            <p>
                <button class="btn btn-small btn-info" id="btnFindPlot">Find Study Locations&nbsp;<i class="icon-search icon-white"></i>
                </button>
            </p>

            <div style="max-height: 200px; overflow-y: auto; margin-bottom: 5px">

                <table class="table table-bordered table-condensed table-striped">
                    <g:if test="${appState?.selectedPlots}">
                        <g:each in="${appState?.selectedPlots}">
                            <tr studyLocationName="${it.name}">
                                <td><a class="studyLocationDetailsLink" href="#">${it.name}</a><button class="btn btn-mini pull-right btnRemoveSelectedPlot" title="Remove study location"><i class="icon-remove"/>
                                </button></td>
                            </tr>
                        </g:each>
                    </g:if>
                    <g:else>
                        <tr>
                            <td>No study locations have been selected</td>
                        </tr>
                    </g:else>
                </table>
            </div>

            <p style="margin-top: 15px">
                <button id="btnComparePlots" class="btn btn-small btn-primary">Compare</button>
                <button id="btnQuestions" class="btn btn-small"><i class="icon-question-sign"></i>&nbsp;Questions</button>
                <button class="btn btn-small" id="btnClearSelection">Remove All</button>
            </p>

        </div>

        <div class="tab-pane" id="studyLocationVisitsTab">
            <table style="width: 100%">
                <tr>
                    <td>
                        <h5>Selected Study Location Visits
                        <g:if test="${appState?.selectedVisits}">
                            (${appState.selectedVisits?.size()})
                        </g:if>
                        </h5>
                    </td>
                    <td style="text-align: right">
                        <button id="btnTogglePlotSelectedVisits" class="btn btn-mini ${appState.plotOnlySelectedLocations ? 'active' : ''}">Show selected only</button>
                    </td>
                </tr>
            </table>

            <p>
                <button class="btn btn-small btn-info" id="btnFindStudyLocationVisits">Find Study Location Visits&nbsp;<i class="icon-search icon-white"></i>
                </button>
            </p>
        </div>

        <div style="max-height: 200px; overflow-y: auto; margin-bottom: 5px">

            <table class="table table-bordered table-condensed table-striped">
                <g:if test="${appState?.selectedVisits}">
                    <g:each in="${appState?.selectedVisits}">
                        <tr studyLocationVisitId="${it.studyLocationVisitId}" studyLocationName="${it.studyLocationName}">
                            <td><a class="studyLocationVisitDetailsLink" href="#"><sts:formatVisitLabel studyLocationVisitId="${it.studyLocationVisitId}" studyLocationName="${it.studyLocationName}" /></a><button class="btn btn-mini pull-right btnRemoveSelectedVisit" title="Remove study location visit"><i class="icon-remove"/>
                            </button></td>
                        </tr>
                    </g:each>
                </g:if>
                <g:else>
                    <tr>
                        <td>No study location visits have been selected</td>
                    </tr>
                </g:else>
            </table>
        </div>


    </div>

</div>

<div id="layersPanel" class="well well-small">
    <table style="width: 100%">
        <tr>
            <td><h5>Environmental layers</h5></td>
            <td style="width: 90px;">
                <button id="btnLayerRemoveAll" class="btn btn-mini btn-danger pull-right">Remove All&nbsp;<i class="icon-remove icon-white"/>
                </button>
            </td>
            <td style="width: 90px;">
                <button id="btnLayerAdd" class="btn btn-mini btn-success pull-right">Add Layer&nbsp;<i class="icon-plus icon-white"/>
                </button>
            </td>
        </tr>
    </table>

    <div id="layersTable" style="overflow-y: scroll;">
        <g:if test="${appState?.layers}">
            <table class="table table-striped table-condensed">
                <g:each in="${appState?.layers}">
                    <tr>
                      <td><small><sts:layerDisplayName class="showLayerInfoLink" href="#" layerName="${it.name}"/></small></td>
                    <td class="toolButtonCell">
                        <button class="btn btn-mini btnToggleLayerVisibility ${it.visible ? 'active' : ''}" layerName="${it.name}" title="Show/Hide this layer in the map"><i class="icon-eye-open"/>
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

    $("#btnTogglePlotSelectedVisits").click(function (e) {
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

    $("#btnComparePlots").click(function (e) {
        e.preventDefault();
        compareSelectedPlots();
    });

    $(".btnRemoveSelectedPlot").click(function (e) {
        e.preventDefault();
        var studyLocationName = $(this).parents('[studyLocationName]').attr("studyLocationName");
        if (studyLocationName) {
            deselectPlot(studyLocationName);
        }
    });

    $(".btnRemoveSelectedVisit").click(function (e) {
        e.preventDefault();
        var studyLocationVisitId = $(this).parents('[studyLocationVisitId]').attr("studyLocationVisitId");
        if (studyLocationVisitId) {
            deselectVisit(studyLocationVisitId);
        }
    });

    $(".studyLocationVisitDetailsLink").click(function(e) {
        e.preventDefault();
        var studyLocationVisitId = $(this).parents('[studyLocationVisitId]').attr("studyLocationVisitId");
        var studyLocationName = $(this).parents('[studyLocationName]').attr("studyLocationName");
        if (studyLocationVisitId) {
            showVisitDetails(studyLocationName, studyLocationVisitId);
        }
    });

    $(".studyLocationDetailsLink").click(function (e) {
        e.preventDefault();
        var studyLocationName = $(this).parents('[studyLocationName]').attr("studyLocationName");
        showPlotDetails(studyLocationName);
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

    $("#btnClearSelection").click(function (e) {
        e.preventDefault();
        clearSelectedPlots();
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
            if ($(this).hasClass("active")) {
                $.ajax("${createLink(controller: 'map', action: 'ajaxSetLayerVisibility')}?layerName=" + layerName + '&visibility=false').done(function (e) {
                    unloadWMSLayer(layerName);
                    element.removeClass("active");
                });
            } else {
                $.ajax("${createLink(controller: 'map', action: 'ajaxSetLayerVisibility')}?layerName=" + layerName + '&visibility=true').done(function (e) {
                    loadWMSLayer(layerName, e.opacity);
                    element.addClass("active");
                });
            }
        }
    });

    $(".btnLayerTools").click(function (e) {
        var element = $(this);
        var layerName = element.attr("layerName");

        var isCurrentActive = $(this).hasClass("active");

        $(".btnLayerTools").removeClass("active");
        $(".layerToolsRow").hide();
        $(".layerToolsCell").html("");

        if (layerName) {
            if (!isCurrentActive) {
                element.addClass("active");
                $("#layerTools_" + layerName).show();
                $.ajax("${createLink(controller: 'map', action: 'layerToolsFragment')}?layerName=" + layerName).done(function (content) {
                    $("#layerTools_" + layerName + " > td").html(content);
                });
            }
        }

    });

    function resizeLayerPanel() {
        var mapHeight = $("#mapContent").height() + $("#mapContent").offset().top;
        var panelTop = $("#layersPanel").offset().top;
        var height = mapHeight - 20 - panelTop;
        $("#layersPanel").height(height);
        $("#layersTable").height(height - 40);
    }

    $(window).resize(function (e) {
        resizeLayerPanel();
    });

    $(".showLayerInfoLink").click(function (e) {
        e.preventDefault();
        var layerName = $(this).attr("layerName");
        displayLayerInfo(layerName);
    });

    $(".mapSelectModeTab").click(function(e) {

        var selectionMode = $(this).attr("mapSelectionMode");
        if (selectionMode) {
            $.ajax("${createLink(contoller:'map', action:'ajaxSetMapSelectionMode')}?mapSelectionMode=" + selectionMode).done(function(e) {
                refreshStudyLocationPoints();
            });
        }

    });

    resizeLayerPanel();

    var selectedTab = $('a[mapSelectionMode="${appState.mapSelectionMode}"]');
    if (selectedTab) {
        selectedTab.click();
    }

</script>
