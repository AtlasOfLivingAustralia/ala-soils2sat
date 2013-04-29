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
