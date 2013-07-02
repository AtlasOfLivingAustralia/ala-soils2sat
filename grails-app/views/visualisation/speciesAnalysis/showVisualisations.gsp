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

<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="detail"/>
        <title>Species Analysis</title>
    </head>

    <body>

        <style type="text/css">
        </style>

        <script type="text/javascript">

            $(document).ready(function () {
            });

        </script>

        <div class="container-fluid">
            <legend>
                <table style="width:100%">
                    <tr>
                        <td>
                            <sts:homeBreadCrumb />
                            <sts:navSeperator/>
                            <span class="sts-breadcrumb">${flowTitle}</span>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </legend>

            <div class="row-fluid">
                <div class="span12">
                    <g:form>
                        <div class="well well-small">
                            <h4>Analysis of <i>${speciesName}</i> for selected study locations</h4>
                            <table class="table table-bordered" style="background-color: rgb(249, 249, 249)">
                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'speciesAnalysisLatitude', params:[studyLocationNames: studyLocationNames?.join(","), speciesName:speciesName])}"></div>
                                    </td>
                                </tr>

                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'speciesAnalysisPointInterceptCounts', params:[studyLocationNames: studyLocationNames?.join(","), speciesName:speciesName])}"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'speciesAnalysisSoilPh', params:[studyLocationNames: studyLocationNames?.join(","), speciesName:speciesName])}"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'compareScalarLayer', params:[layerName:'rhu215_m', color: '#4AACC7'])}"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'compareScalarLayer', params:[layerName:'rainm', color: '#F69646'])}"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="visualisation" visLink="${createLink(action:'compareScalarLayer', params:[layerName:'arid_mean', color: '#C0504D'])}"></div>
                                    </td>
                                </tr>
                            </table>
                            <div>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>&nbsp;</th>
                                            <g:each in="${studyLocationNames}" var="studyLocation">
                                                <th>${studyLocation}</th>
                                            </g:each>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:each in="${layerData.fieldNames}" var="layerName">
                                            <tr>
                                                <td><strong>${layerName}</strong></td>
                                                <g:each in="${studyLocationNames}" var="studyLocation">
                                                    <td>${layerData.data[studyLocation][layerName]}</td>
                                                </g:each>

                                            </tr>
                                        </g:each>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <g:link class="btn btn-small" event="back"><i class="icon-chevron-left"></i>&nbsp;Previous</g:link>
                        <g:link class="btn btn-small btn-primary" event="finish">Return to Map</g:link>
                    </g:form>
                </div>
            </div>
        </div>
        <script type="text/javascript">

            $(".visualisation").each(function() {
                var visLink =$(this).attr("visLink");
                if (visLink) {
                    $(this).html('<img src="../images/spinner.gif" />&nbsp;Loading...');
                    var targetElement = $("[visLink='" + visLink + "']");
                    $.ajax(visLink).done(function(html) {
                        targetElement.html(html);
                    });
                }
            });

        </script>
    </body>
</html>
