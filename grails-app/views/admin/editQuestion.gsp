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
        <content tag="pageTitle">Matrix</content>
        <content tag="adminButtonBar">
        </content>

        <script type="text/javascript">

            $(document).ready(function() {
                $("#btnDelete").click(function(e) {
                    e.preventDefault();
                    if (confirm("Are you sure you want to delete this question?")) {
                        window.location = "${createLink(action:"deleteQuestion", params:[questionId:question.id])}";
                    }
                });
            });

        </script>
        <content tag="adminButtonBar">
            <a class="btn btn-small" href="${createLink(controller: 'admin', action:'matrix')}">Back to matrix</a>
        </content>
        <g:form class="form-horizontal" action="updateQuestion">
            <div class="well well-small">
                <h5>Edit Question</h5>

                <g:hiddenField name="questionId" value="${question.id}" />

                <div class="control-group">
                    <label class="control-label" for='question'>Question:</label>
                    <div class="controls">
                        <g:textField class="input-xlarge" name="question" placeholder="Question text" value="${question.text}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='description'>Description:</label>

                    <div class="controls">
                        <g:textField class="input-xlarge" name="description" placeholder="Description" value="${question.description}"/>
                    </div>
                </div>

                <div class="control-group">
                    <div class="controls">
                        <g:submitButton class="btn btn-primary" name="submit" value='Update'/>
                        <button id="btnDelete" type="button" class="btn btn-danger">Delete</button>
                    </div>
                </div>

            </div>

        </g:form>

    </body>
</html>
