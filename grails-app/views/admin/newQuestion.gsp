<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="admin"/>
        <title>Soils to Satellites - Admin - Matrix</title>
    </head>

    <body>
        <content tag="pageTitle">Matrix</content>
        <content tag="adminButtonBar">
        </content>

        <script type="text/javascript">

            $("#btnAddQuestion").click(function(e) {
                e.preventDefault();
                window.location = "${createLink(controller: 'admin', action:'newQuestion')}";
            });

        </script>
        <content tag="adminButtonBar">
            <a class="btn btn-small" href="${createLink(controller: 'admin', action:'matrix')}">Back to matrix</a>
        </content>
        <g:form class="form-horizontal" action="insertQuestion">
            <div class="well well-small">
                <h5>New Question</h5>

                <div class="control-group">
                    <label class="control-label" for='question'>Question:</label>
                    <div class="controls">
                        <g:textField class="input-xlarge" name="question" placeholder="Question text" value="${params.question}"/>
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
                        <g:submitButton class="btn btn-primary" name="submit" value='Save'/>
                    </div>
                </div>

            </div>

        </g:form>

    </body>
</html>
