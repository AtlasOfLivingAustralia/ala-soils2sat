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

<legend style="margin-bottom: 3px;">
    <table style="width: 100%">
        <tr>
            <td>Compare Study Locations (${appState?.selectedPlotNames?.size()})</td>
            <td style="text-align: right">
                <button id="btnCompareExport" class="btn btn-small">Export data</button>
                <button id="btnCloseCompare" class="btn btn-small">Close</button>
            </td>
        </tr>
    </table>
</legend>
<g:set var="max_width" value="790"/>
<g:set var="max_height" value="500"/>

<g:if test="${appState?.layers?.size() >= 1 && appState?.selectedPlotNames?.size() > 1}">
    <div class="tabbable">
      <ul class="nav nav-tabs" style="margin-bottom: 0px">
        <li class="active"><a href="#layerData" data-toggle="tab">Layers</a></li>
        <li><a href="#taxaData" data-toggle="tab">Taxa</a></li>
      </ul>
      <div class="tab-content">

        <div class="tab-pane active" id="layerData" >
          <div style="max-height: ${max_height}px; max-width: ${max_width}px; overflow: scroll;">
    <table class="table table-striped table-condensed">
        <thead>
            <tr>
                <th></th>
                <g:each in="${appState.selectedPlotNames}" var="studyLocation">
                    <th>${studyLocation}</th>
                </g:each>
            </tr>
        </thead>
        <tbody>
            <g:each in="${results.fieldNames}" var="fieldName">
                <tr>
                    <td>${fieldName}
                    <g:if test="${results.fieldUnits[fieldName]}">
                        &nbsp;(${results.fieldUnits[fieldName]})
                    </g:if>
                    </td>
                    <g:each in="${appState?.selectedPlotNames}" var="studyLocation">
                        <td>${results.data[studyLocation][fieldName]}</td>
                    </g:each>
                </tr>
            </g:each>
        </tbody>
    </table>
    </div>
  </div>

    <div class="tab-pane" id="taxaData">
        <div>
            <button class="btn btn-small active btnDiffMode" title="Show all taxa" diffMode="none"><i class="icon-ok-sign"></i></button>
            <button class="btn btn-small btnDiffMode" diffMode="intersect" title="Show only taxa that occur at every study location (intersection)"><img src="${resource(dir: '/images', file: 'intersect.png')}" height="14" width="14"/>
            </button>
            <button class="btn btn-small btnDiffMode" diffMode="inverseIntersect" title="Show only unique taxa at each study location (union minus intersection) "><img src="${resource(dir: '/images', file: 'inverseIntersect.png')}" height="14" width="14"/>
            </button>
        </div>

        <div id="taxaContent" style="max-height: ${Integer.parseInt(max_height) - 40}px; max-width: ${max_width}px; overflow: scroll;">
        </div>
    </div>

    </div>
</g:if>
<g:else>
    <p>You must first select at least one map layer, and two or more study locations before using this feature.
    </p>
</g:else>
</div>

<script type="text/javascript">

    $("#btnCompareExport").click(function (e) {
        e.preventDefault();
        location.href = "${createLink(controller: 'studyLocation', action:'exportComparePlots')}";
    });

    $("#btnCloseCompare").click(function (e) {
        e.preventDefault();
        hideModal();
    });

    $('a[data-toggle="tab"]').on('shown', function (e) {
        var tabHref = $(this).attr('href');
        if (tabHref == '#taxaData') {
            loadTaxaCompare();
        }

    });

    $(".btnDiffMode").click(function (e) {
        $(".btnDiffMode").removeClass('active');
        $(this).addClass('active');
        loadTaxaCompare();
    });

    function loadTaxaCompare() {
        $("#taxaContent").html("Loading...");
        var diffMode = $('.btnDiffMode.active').attr("diffMode");

        $.ajax("${createLink(controller:'studyLocation', action:'compareTaxaFragment')}?diffMode=" + diffMode).done(function (content) {
            $("#taxaContent").html(content);
        });

    }

</script>