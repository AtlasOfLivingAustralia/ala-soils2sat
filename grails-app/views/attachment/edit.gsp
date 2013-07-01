<%@ page import="au.org.ala.soils2sat.AttachmentCategory" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Attachment</title>
    </head>

    <body>

        <content tag="pageTitle">Attachments</content>

        <div id='login' class="container-fluid">

            <h3>Edit Attachment</h3>

            <g:form class="form-horizontal" controller="attachment" action="saveAttachment" enctype="multipart/form-data">

                <g:hiddenField name="id" value="${attachment?.id}" />

                <g:render template="form" model="${[attachment:attachment]}" />

                <div class="control-group">
                    <div class="controls">
                        <button type="submit" class="btn btn-small btn-primary">Save</button>
                    </div>
                </div>

            </g:form>
        </div>
    </body>
</html>