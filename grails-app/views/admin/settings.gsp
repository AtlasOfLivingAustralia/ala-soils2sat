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
        <script type="text/javascript">

            $(document).ready(function() {
            });

        </script>
        <content tag="pageTitle">Settings</content>
        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th>Setting</th>
                    <th>Value</th>
                    <th>Comment</th>
                    <th style="min-width: 60px"></th>
                </tr>
            </thead>
            <tbody>
                <g:each var="setting" in="${settings}">
                    <tr>
                        <td>
                            ${setting.key}
                        </td>
                        <td>
                            ${setting.value}
                        </td>
                        <td>
                            ${setting.comment}
                        </td>
                        <td>
                            <a href="${createLink(controller:'admin', action:'editSetting', id: setting.id)}" class="btn btn-mini" title="Edit this setting">
                                <i class="icon-edit"></i>
                            </a>
                            <a href="${createLink(controller:'admin', action:'deleteSetting', id: setting.id)}" class="btn btn-danger btn-mini" title="Delete this setting (revert to default)">
                                <i class="icon-remove icon-white"></i>
                            </a>
                        </td>
                    </tr>
                </g:each>

            </tbody>
        </table>
    </body>
</html>