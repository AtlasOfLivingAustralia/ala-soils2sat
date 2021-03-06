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
        <title>Extract Data from Study Location Visits</title>
    </head>

    <body>

        <style type="text/css">
        </style>

        <script type="text/javascript">

            $(document).ready(function () {
            });

        </script>

        <div class="container">
            <legend>
                <table style="width:100%">
                    <tr>
                        <td>
                            <sts:homeBreadCrumb />
                            <sts:navSeperator/>
                            <span class="sts-breadcrumb">Extract Data from selected Study Location Visits</span>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </legend>

            <div class="row">
                <div class="span12">
                    <g:form>
                        <div class="well well-small">
                        <h4>Data Extraction Results</h4>
                            Success: ${extractionResults.success}
                            <ul>
                                <li>Package: <a href="${extractionResults.packageUrl}">Download zip file</a></li>
                                <li>DOI: ${extractionResults.DOI}</li>
                                <li>RIF/CS Receipt info</li>
                                <li>Creator info : ${creatorSurname}, ${creatorGivenNames}</li>
                            </ul>
                        </div>

                        <g:link class="btn btn-small btn-primary" event="finish">Return to Map</g:link>

                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>
