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
        <title>Soils to Satellites - Admin - Settings</title>
    </head>

    <body>
        <content tag="pageTitle">Settings</content>
        <content tag="adminButtonBar">
        </content>

        <script type="text/javascript">

            $(document).ready(function() {
            });

        </script>
        <content tag="adminButtonBar">
        </content>

        <g:form class="form-horizontal" action="updateSetting">
            <div class="well well-small">
                <h5>Edit Setting</h5>

                <div class="alert alert-info">
                    ${setting.comment}
                </div>


                <g:hiddenField name="id" value="${setting.id}" />

                <div class="control-group">
                    <label class="control-label" for='question'>Setting Key:</label>
                    <div class="controls">
                        <g:textField readonly="true" class="input-xlarge" name="key" value="${setting?.key}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='description'>Value:</label>

                    <div class="controls">
                        <g:textField class="input-xlarge" name="value" placeholder="value" value="${setting.value}" />
                    </div>
                </div>


                <div class="control-group">
                    <div class="controls">
                        <g:submitButton class="btn btn-primary" name="submit" value='Update'/>
                    </div>
                </div>

            </div>

        </g:form>

    </body>
</html>
