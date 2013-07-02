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
        <title>Soils to Satellites - Admin - Matrix</title>
    </head>

    <body>

        <style type="text/css">

        td.value-cell.value-required {
            background-color: #98fb98 !important;
        }

        td.value-not-required {
            background-color: #FF4D4D !important;
        }

        td.value-cell {
            text-align: center !important;
            vertical-align: middle !important;
        }

        select.input-mini {
            margin-bottom: 0;
            height: inherit;
            width: 45px;
        }

        </style>

        <content tag="pageTitle">Matrix</content>

        <content tag="adminButtonBar">
            <button id="btnImport" class="btn btn-small"><i class="icon-upload"></i>&nbsp;Import matrix</button>
            <button id="btnExport" class="btn btn-small"><i class="icon-download-alt"></i>&nbsp;Export matrix</button>
            <button id="btnAddQuestion" class="btn btn-small btn-primary"><i class="icon-plus icon-white"></i>&nbsp;Add question</button>
        </content>

        <table class="table table-bordered table-striped table-condensed">
            <thead>
                <tr>
                    <th>Ecological Context</th>
                    <g:each in="${questions}" var="question">
                        <th questionId="${question.id}">
                            <small><a href="${createLink(action:'editQuestion', params:[questionId: question.id])}">${question.text}</a></small>
                        </th>
                    </g:each>
                </tr>
            </thead>
            <tbody>
                <g:each in="${contexts}" var="context">
                    <tr>
                        <td style="width: 250px"><small><a href="${createLink(action:'editEcologicalContext', params:[ecologicalContextId: context.id])}">${context.name}</a></small></td>
                        <g:each in="${questions}" var="question">
                            <g:set var="matrixValue" value="${valueMap["${context.id}_${question.id}"]}" />
                            <g:set var="required" value="${matrixValue ? (matrixValue.required ? 'Y' : 'N')  : ''}" />
                            <g:set var="theClass" value=""/>
                            <g:if test="${required}">
                                <g:set var="theClass" value="${required == 'Y' ? 'value-required' : 'value-not-required'}" />
                            </g:if>

                            <td class="value-cell ${theClass}" questionId="${question.id}" ecologicalContextId="${context.id}">
                                <small>
                                <g:select class="input-mini cmbValue" name="value_${context.id}_${question.id}" from="${['', 'Y', 'N']}" value="${required}" />
                                </small>
                            </td>
                        </g:each>
                    </tr>
                </g:each>
            </tbody>
        </table>

        <script type="text/javascript">

            $(document).ready(function() {

                $("#btnExport").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(action:'exportMatrix')}";
                });

                $("#btnImport").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(action:'selectImportFile', params:[importType: 'matrix'])}";
                });

                $("#btnAddQuestion").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(controller: 'admin', action:'newQuestion')}";
                });

                $(".btnDeleteQuestion").click(function(e) {
                    e.preventDefault();
                    var questionId = $(this).parents("[questionId]").attr("questionId");
                    if (questionId) {
                        if (confirm("Are sure you wish to delete this question?")) {
                            window.location ="${createLink(controller:'admin', action:'deleteQuestion')}?questionId=" + questionId;
                        }
                    }
                });

                $(".btnEditQuestion").click(function(e) {
                    e.preventDefault();
                    var questionId = $(this).parents("[questionId]").attr("questionId");
                    if (questionId) {
                        window.location ="${createLink(controller:'admin', action:'editQuestion')}?questionId=" + questionId;
                    }
                });

                $(".cmbValue").change(function(e) {
                    var val = $(this).val();
                    var td = $(this).parents("td");
                    var questionId = td.attr("questionId");
                    var contextId = td.attr("ecologicalContextId");

                    if (questionId && contextId) {

                        var url = "${createLink(controller:'admin', action:'setMatrixValue')}?questionId=" + questionId + "&ecologicalContextId=" + contextId + "&value=" + val;
                        $.ajax(url).done(function() {
                            if (val == 'Y') {
                                td.addClass("value-required");
                                td.removeClass("value-not-required");
                            } else if (val == 'N') {
                                td.removeClass("value-required");
                                td.addClass("value-not-required");
                            } else {
                                td.removeClass("value-required");
                                td.removeClass("value-not-required");
                            }
                        });
                    }
                });

            });

        </script>

    </body>
</html>
