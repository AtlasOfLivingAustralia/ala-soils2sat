<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Search Criteria</title>
    </head>

    <body>
        <content tag="pageTitle">Search Criteria</content>
        <content tag="adminButtonBar">
        </content>

        <content tag="adminButtonBar">
            <a class="btn btn-small" href="${createLink(controller: 'admin', action:'searchCriteria')}">Back to Search Criteria</a>
        </content>
        <g:form class="form-horizontal" action="saveSearchCriteriaDefinition" controller="admin">

            <g:hiddenField name="searchCriteriaDefinitionId" value="${criteriaDefinition?.id}" />

            <div class="well well-small">
                <h5>
                    <g:if test="${criteriaDefinition}">
                        Edit Search Criteria Definition
                    </g:if>
                    <g:else>
                        New Search Criteria Definition
                    </g:else>
                </h5>

                <div class="control-group">
                    <label class="control-label" for='name'>Name:</label>
                    <div class="controls">
                        <g:textField class="input-xlarge" name="name" placeholder="Name" value="${criteriaDefinition?.name}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='description'>Description:</label>
                    <div class="controls">
                        <g:textField class="input-xlarge" name="description" placeholder="Description" value="${criteriaDefinition?.description}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='description'>Type:</label>
                    <div class="controls">
                        <g:select name="type" from="${au.org.ala.soils2sat.CriteriaType.values()}" value="${criteriaDefinition?.type}" />
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='description'>Value Type:</label>
                    <div class="controls">
                        <g:select name="valueType" from="${au.org.ala.soils2sat.CriteriaValueType.values()}" value="${criteriaDefinition?.valueType}" />
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='description'>Field:</label>
                    <div class="controls">
                        <g:textField class="input-xlarge" name="fieldName" placeholder="Field name" value="${criteriaDefinition?.fieldName}"/>
                    </div>
                </div>


                <div class="control-group">
                    <div class="controls">
                        <g:submitButton class="btn btn-primary" name="submit" value='Save'/>
                    </div>
                </div>

            </div>

        </g:form>

        <script type="text/javascript">

            $(document).ready(function(e) {

            });

        </script>

    </body>
</html>
