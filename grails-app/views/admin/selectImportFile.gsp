<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - ${heading}</title>
    </head>

    <body>
        <content tag="pageTitle">${heading}</content>

        <content tag="adminButtonBar">
        </content>

        <div class="">
            <h5>${heading}</h5>
            <g:uploadForm name="importForm" action="${importAction}">
                <input type="file" name="filename" />
                <button type="button" class="btn btn-small" id="btnCancel">Cancel</button>
                <g:if test="${cancelUrl}">
                    <button type="submit" class="btn btn-small btn-primary">Import</button>
                </g:if>
            </g:uploadForm>
        </div>

        <script type="text/javascript">

            $(document).ready(function() {
                $("#btnCancel").click(function(e) {
                    e.preventDefault();
                    window.location = "${cancelUrl}";
                });
            });

        </script>

    </body>
</html>
