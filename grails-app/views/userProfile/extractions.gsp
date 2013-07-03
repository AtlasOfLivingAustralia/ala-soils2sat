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

<%@ page import="au.org.ala.soils2sat.UserProfile" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="profilePage">
		<title>Extractions</title>
	</head>
	<body>

        <script type="text/javascript">

            $(document).ready(function() {
            });

        </script>

        <content tag="pageTitle">Extractions</content>
        <small>
            <strong>Note:</strong> Only those extractions that have DOI's will be made available to the ANDS RIF/CS harvester.
        </small>

        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <g:sortableColumn title="Date" property="date" />
                    <g:sortableColumn title="Package Name" property="packageName" />
                    <g:sortableColumn title="DOI" property="doi" />
                    %{--<g:sortableColumn title="Username" property="username" />--}%
                    <g:sortableColumn title="Downloads" property="downloadCount" />
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${extractions}" var="extraction">
                    <tr packageName="${extraction.packageName}">
                        <td><g:formatDate date="${extraction.date}" format="${au.org.ala.soils2sat.DateUtils.S2S_DATE_TIME_FORMAT}" /> </td>
                        <td>${extraction.packageName}</td>
                        <td>${extraction.doi ?: 'N/A'}</td>
                        %{--<td>${extraction.username}</td>--}%
                        <td>${extraction.downloadCount}</td>
                        <td>
                            <a class="btn btn-mini" href="${createLink(controller:'extract', action:'landingPage', params:[packageName: extraction.packageName])}" title="Package landing page">
                                <i class="icon-home"></i>
                            </a>
                            <a class="btn btn-mini" href="${createLink(controller: 'extract',action:'downloadPackage', params:[packageName: extraction.packageName])}" title="Download package">
                                <i class="icon-download"></i>
                            </a>
                            <g:if test="${!extraction.doi}">
                                <a class="btn btn-mini" href="${createLink(controller: 'extract',action:'mintDOI', params:[packageName: extraction.packageName])}" title="Mint a DOI for this package">
                                    Mint DOI
                                </a>
                            </g:if>
                            <button class="btn btn-mini btn-danger btnDelete" title="Delete package"><i class="icon-remove icon-white"></i></button>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>
        <div class="pagination">
            <g:paginate total="${extractions.totalCount}" omitNext="true" omitPrev="true" />
        </div>

	</body>
</html>
