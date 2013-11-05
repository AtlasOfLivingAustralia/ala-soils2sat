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
		<g:set var="entityName" value="${message(code: 'userProfile.label', default: 'UserProfile')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>

        <script type="text/javascript">
            $(document).ready(function() {

                $("#btnCancel").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(controller:'map')}";
                });

            });

        </script>

        <content tag="pageTitle">Details</content>

        <content tag="profileButtonBar">
            Citation Profile (${user.username})
        </content>


        <div class="container">
            <g:hasErrors bean="${userProfile}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${userProfile}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
            </g:hasErrors>

            <div class="row">
                <div class="span12">
                    <g:form method="post" class="form-horizontal" >
                        <g:hiddenField name="userId" value="${user.id}" />
                        <g:hiddenField name="id" value="${userProfile?.id}" />
                        <g:hiddenField name="version" value="${userProfile?.version}" />
                        <fieldset class="form">
                            <g:render template="form"/>
                        </fieldset>
                        <fieldset class="buttons">
                            <g:actionSubmit class="btn btn-primary" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                            <button class="btn" id="btnCancel">Cancel</button>
                        </fieldset>
                    </g:form>
                </div>
            </div>
        </div>
	</body>
</html>
