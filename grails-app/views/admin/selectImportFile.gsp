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
        <title>Soils to Satellites - Admin - ${heading}</title>
    </head>

    <body>
        <content tag="pageTitle">${heading}</content>

        <content tag="adminButtonBar">
        </content>

        <div class="">
            <h5>${heading}</h5>
            <g:uploadForm name="importForm" action="${importAction}">
                <input type="file" name="filename" />
                <button type="button" class="btn btn-small" id="btnCancel">Cancel</button>
                <g:if test="${cancelUrl}">
                    <button type="submit" class="btn btn-small btn-primary">Import</button>
                </g:if>
            </g:uploadForm>
        </div>

        <script type="text/javascript">

            $(document).ready(function() {
                $("#btnCancel").click(function(e) {
                    e.preventDefault();
                    window.location = "${cancelUrl}";
                });
            });

        </script>

    </body>
</html>
