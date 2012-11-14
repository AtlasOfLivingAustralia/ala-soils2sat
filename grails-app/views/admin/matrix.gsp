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
        <table class="table table-bordered table-striped table-condensed">
            <thead>
                <tr>
                    <th>Ecological Context</th>
                    <g:each in="${questions}" var="question">
                        <th questionId="${question.id}">${question.text}&nbsp;<button class="btn btn-mini btn-danger btnDeleteQuestion"><i class="icon-remove icon-white"></i></button></th>
                    </g:each>
                </tr>
            </thead>
            <tbody>
                <g:each in="${contexts}" var="context">
                    <tr>
                        <td style="width: 300px">${context.toString()}</td>
                        <g:each in="${questions}" var="question">
                        <td></td>
                        </g:each>
                    </tr>
                </g:each>
            </tbody>
        </table>
        <content tag="adminButtonBar">
            <button id="btnAddQuestion" class="btn btn-small btn-primary"><i class="icon-plus icon-white"></i>&nbsp;Add question</button>
        </content>

        <script type="text/javascript">

            $("#btnAddQuestion").click(function(e) {
                e.preventDefault();
                window.location = "${createLink(controller: 'admin', action:'newQuestion')}";
            });

            $(".btnDeleteQuestion").click(function(e) {
                e.preventDefault();
                var questionId = $(this).parents("[questionId]").attr("questionId");
                if (questionId) {
                    if (confirm("Are sure you wish to delete this question?")) {
                        window.location ="${createLink(controller:'admin', action:'deleteQuestion')}?questionId=" + questionId;
                    }
                }
            });

        </script>

    </body>
</html>
