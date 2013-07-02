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

<%@ page import="au.org.ala.soils2sat.AttachmentOwnerType; au.org.ala.soils2sat.AttachmentCategory" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Attachment</title>
    </head>

    <body>

        <content tag="pageTitle">Attachments</content>

        <div id='login' class="container-fluid">

            <h3>Upload Attachment</h3>

            <g:form class="form-horizontal" controller="attachment" action="saveAttachment" enctype="multipart/form-data">

                <g:render template="form" model="${[attachment: attachment]}" />

                <div class="control-group">
                    <div class="controls">
                        <button type="submit" class="btn btn-small btn-primary"><i class="icon-upload icon-white"></i>&nbsp;Upload</button>
                    </div>
                </div>

            </g:form>
        </div>
    </body>
</html>