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
        <title>Questions</title>
    </head>
    <body>
        <div class="container">

            <legend>
                <table style="width:100%">
                    <tr>
                        <td>Questions</td>
                        <td style="text-align: right"></td>
                    </tr>
                </table>
            </legend>

            <div class="alert alert-info">
                <strong>Selected study locations:</strong>
                ${userInstance.applicationState.selectedPlotNames.collect({ it }).join(", ")}
            </div>

            <div class="well well-small">
                <h4>Select a question:</h4>
                <g:if test="${!questions}">
                    <span class="alert alert-danger">No questions have been defined yet</span>
                </g:if>
                <g:else>
                    <ul class="nav nav-pills nav-stacked">
                        <g:each in="${questions}" var="question">
                            <li><a href="${createLink(controller:'question', action:'ecologicalContexts', params:[questionId:question.id])}">${question.text}</a></li>
                        </g:each>
                    </ul>
                </g:else>

            </div>

        </div>
        <script type="text/javascript">
        </script>
    </body>
</html>