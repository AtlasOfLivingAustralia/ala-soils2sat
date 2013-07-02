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

<%@ page import="org.apache.commons.lang.WordUtils" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="detail"/>
        <title>Compare Study Locations</title>
    </head>

    <body>

        <script type="text/javascript">

            $(document).ready(function() {

                $("#btnCompareExport").click(function (e) {
                    e.preventDefault();
                    location.href = "${createLink(controller: 'studyLocation', action:'exportComparePlots')}";
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

                $.ajax("${createLink(controller: 'visualisation', action:'compareStudyLocationVisualisations', params:[])}").done(function(html) {
                    $("#comparisonVisualisations").html(html);
                });

            });

            function loadTaxaCompare() {
                $("#taxaContent").html("Loading...");
                var diffMode = $('.btnDiffMode.active').attr("diffMode");

                $.ajax("${createLink(controller:'studyLocation', action:'compareTaxaFragment')}?diffMode=" + diffMode).done(function (content) {
                    $("#taxaContent").html(content);
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
                            <span class="sts-breadcrumb">Compare study locations (${appState?.selectedPlotNames?.size()})</span>
                        </td>
                        <td>
                            <button id="btnCompareExport" class="btn btn-small">Export data</button>
                        </td>
                    </tr>
                </table>
            </legend>

            <g:if test="${appState?.layers?.size() >= 1 && appState?.selectedPlotNames?.size() > 1}">
                <div class="tabbable">
                    <ul class="nav nav-tabs" style="margin-bottom: 0px">
                        <li class="active"><a href="#layerData" data-toggle="tab">Layers</a></li>
                        <li><a href="#taxaData" data-toggle="tab">Taxa</a></li>
                    </ul>

                    <div class="tab-content">
                        <div class="tab-pane active" id="layerData" >
                            <div>
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
                            <div id="comparisonVisualisations" class="visualisationContainer">
                                <sts:spinner />
                            </div>
                        </div>
                        <div class="tab-pane" id="taxaData">
                            <div>
                                <button class="btn btn-small active btnDiffMode" title="Show all taxa" diffMode="none"><i class="icon-ok-sign"></i></button>
                                <button class="btn btn-small btnDiffMode" diffMode="intersect" title="Show only taxa that occur at every study location (intersection)"><img src="${resource(dir: '/images', file: 'intersect.png')}" height="14" width="14"/></button>
                                <button class="btn btn-small btnDiffMode" diffMode="inverseIntersect" title="Show only unique taxa at each study location (union minus intersection) "><img src="${resource(dir: '/images', file: 'inverseIntersect.png')}" height="14" width="14"/></button>
                            </div>
                            <div id="taxaContent">
                            </div>
                        </div>
                    </div>
                </g:if>
                <g:else>
                    <p>You must first select at least one environmental layer, and two or more study locations before using this feature.</p>
                </g:else>
            </div>
        </div>
    </body>
</html>