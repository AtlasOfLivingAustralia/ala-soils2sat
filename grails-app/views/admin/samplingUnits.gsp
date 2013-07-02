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
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Sampling Units</title>
    </head>

    <body>
        <content tag="pageTitle">Sampling Units</content>

        <content tag="adminButtonBar">
            <button id="btnDeleteAll" class="btn btn-danger btn-small"><i class="icon-remove icon-white"></i>&nbsp;Delete all sampling units</button>
            <button id="btnImport" class="btn btn-small" style="margin-right: 10px"><i class="icon-upload"></i>&nbsp;Import from csv</button>
            <button id="btnExport" class="btn btn-small"><i class="icon-download-alt"></i>&nbsp;Export to csv</button>
        </content>

        <div class="">
            <div class="control-group">
                <div class="controls">
                    <g:textField id="samplingUnit" style="margin-bottom: 0" name="unit" class="input-xlarge"/>
                    <button id="btnAddUnit" class="btn btn-primary"><i class="icon-plus icon-white"></i>&nbsp;Add</button>
                </div>
            </div>
            <div id="samplingUnits">
                <g:if test="${samplingUnits}">
                    <table class="table table-condensed table-striped table-bordered">
                        <g:each in="${samplingUnits}" var="unit">
                            <tr samplingUnitId="${unit.id}">
                                <td>${unit.name}</td>
                                <td><button class="btn btn-mini btn-danger btnDeleteUnit"><i class="icon-remove icon-white"></i>&nbsp;delete</button></td>
                            </tr>
                        </g:each>
                    </table>
                </g:if>
                <g:else>
                    <div class="alert alert-info">
                        No Sampling Units have been defined yet
                    </div>
                </g:else>
            </div>
        </div>

        <script type="text/javascript">
            $(document).ready(function() {

                $("#btnAddUnit").click(function(e) {
                    e.preventDefault();
                    var name = $("#samplingUnit").val();
                    if (name) {
                        window.location = "${createLink(controller:'admin', action:'addSamplingUnit')}?name=" + name;
                    }
                });

                $(".btnDeleteUnit").click(function(e) {
                    e.preventDefault();
                    var unitId = $(this).parents("[samplingUnitId]").attr("samplingUnitId");
                    if (unitId) {
                        window.location = "${createLink(controller:'admin', action:'deleteSamplingUnit')}?samplingUnitId=" + unitId;
                    }
                });

                $("#btnExport").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(controller:'admin', action:'exportSamplingUnits')}";
                });

                $("#btnImport").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(controller:'admin', action:'selectImportFile', params:[importType:'samplingUnits'])}";
                });

                $("#btnDeleteAll").click(function(e) {
                    e.preventDefault();
                    if (confirm("Are you sure you wish to delete all Sampling Units?")) {
                        window.location = "${createLink(action:"deleteAllSamplingUnits")}";
                    }
                });

            });
        </script>

    </body>
</html>
