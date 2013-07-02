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

<div class="control-group fieldcontain ${hasErrors(bean: userProfile, field: 'surname', 'error')} ">
	<label class="control-label" for="surname">
		<g:message code="userProfile.surname.label" default="Surname" />
	</label>
    <div class="controls">
	    <g:textField name="surname" value="${userProfile?.surname}"/>
    </div>
</div>

<div class="control-group fieldcontain ${hasErrors(bean: userProfile, field: 'givenNames', 'error')} ">
	<label class="control-label" for="givenNames">
		<g:message code="userProfile.givenNames.label" default="Given Names" />
	</label>
    <div class="controls">
	    <g:textField name="givenNames" value="${userProfile?.givenNames}"/>
    </div>
</div>

<div class="control-group fieldcontain ${hasErrors(bean: userProfile, field: 'institution', 'error')} ">
	<label class="control-label" for="institution">
		<g:message code="userProfile.institution.label" default="Institution" />
	</label>
    <div class="controls">
        <g:textField name="institution" value="${userProfile?.institution}"/>
    </div>
</div>

<div class="control-group fieldcontain ${hasErrors(bean: userProfile, field: 'contactNumber', 'error')} ">
	<label class="control-label" for="contactNumber">
		<g:message code="userProfile.contactNumber.label" default="Contact Number" />
	</label>
    <div class="controls">
	    <g:textField name="contactNumber" value="${userProfile?.contactNumber}"/>
    </div>
</div>
