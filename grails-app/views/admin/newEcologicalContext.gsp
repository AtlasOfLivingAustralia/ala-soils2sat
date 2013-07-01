<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Ecological Contexts</title>
    </head>

    <body>
        <content tag="pageTitle">Ecological Contexts</content>
        <content tag="adminButtonBar">
        </content>

        <content tag="adminButtonBar">
            <a class="btn btn-small" href="${createLink(controller: 'admin', action:'ecologicalContexts')}">Back to contexts</a>
        </content>
        <g:form class="form-horizontal" action="insertEcologicalContext">
            <div class="well well-small">
                <h5>New Ecological Context</h5>

                <div class="control-group">
                    <label class="control-label" for='name'>Name:</label>
                    <div class="controls">
                        <g:textField class="input-xlarge" name="name" placeholder="Name" value="${params.name}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='description'>Description:</label>

                    <div class="controls">
                        <g:textField class="input-xlarge" name="description" placeholder="Description" value="${params.description}"/>
                    </div>
                </div>

                <div class="control-group">
                    <div class="controls">
                        <g:submitButton class="btn btn-primary" name="submit" value='Add'/>
                    </div>
                </div>

            </div>

        </g:form>

    </body>
</html>
