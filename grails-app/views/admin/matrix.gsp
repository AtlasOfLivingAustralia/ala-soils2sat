<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Matrix</title>
    </head>

    <body>

        <style type="text/css">

        td.value-cell.value-required {
            background-color: #98fb98 !important;
        }

        td.value-not-required {
            background-color: #FF4D4D !important;
        }

        td.value-cell {
            text-align: center !important;
            vertical-align: middle !important;
        }

        select.input-mini {
            margin-bottom: 0;
            height: inherit;
            width: 45px;
        }

        </style>

        <content tag="pageTitle">Matrix</content>

        <table class="table table-bordered table-striped table-condensed">
            <thead>
                <tr>
                    <th>Ecological Context</th>
                    <g:each in="${questions}" var="question">
                        <th questionId="${question.id}">
                            <small>${question.text}</small>
                            <br />
                            <button class="btn btn-mini btn-danger btnDeleteQuestion"><i class="icon-trash icon-white"></i></button>
                            <button class="btn btn-mini btnEditQuestion"><i class="icon-edit"></i></button>
                        </th>
                    </g:each>
                </tr>
            </thead>
            <tbody>
                <g:each in="${contexts}" var="context">
                    <tr>
                        <td style="width: 250px"><small>${context.name}</small></td>
                        <g:each in="${questions}" var="question">
                            <g:set var="matrixValue" value="${valueMap["${context.id}_${question.id}"]}" />
                            <g:set var="required" value="${matrixValue ? (matrixValue.required ? 'Y' : 'N')  : ''}" />
                            <g:set var="theClass" value=""/>
                            <g:if test="${required}">
                                <g:set var="theClass" value="${required == 'Y' ? 'value-required' : 'value-not-required'}" />
                            </g:if>

                            <td class="value-cell ${theClass}" questionId="${question.id}" ecologicalContextId="${context.id}">
                                <small>
                                <g:select class="input-mini cmbValue" name="value_${context.id}_${question.id}" from="${['', 'Y', 'N']}" value="${required}" />
                                </small>
                            </td>
                        </g:each>
                    </tr>
                </g:each>
            </tbody>
        </table>

        <content tag="adminButtonBar">
            <button id="btnAddQuestion" class="btn btn-small btn-primary"><i class="icon-plus icon-white"></i>&nbsp;Add question</button>
        </content>

        <script type="text/javascript">

            $(document).ready(function() {

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

                $(".btnEditQuestion").click(function(e) {
                    e.preventDefault();
                    var questionId = $(this).parents("[questionId]").attr("questionId");
                    if (questionId) {
                        window.location ="${createLink(controller:'admin', action:'editQuestion')}?questionId=" + questionId;
                    }
                });

                $(".cmbValue").change(function(e) {
                    var val = $(this).val();
                    var td = $(this).parents("td");
                    var questionId = td.attr("questionId");
                    var contextId = td.attr("ecologicalContextId");

                    if (questionId && contextId) {

                        var url = "${createLink(controller:'admin', action:'setMatrixValue')}?questionId=" + questionId + "&ecologicalContextId=" + contextId + "&value=" + val;
                        $.ajax(url).done(function() {
                            if (val == 'Y') {
                                td.addClass("value-required");
                                td.removeClass("value-not-required");
                            } else if (val == 'N') {
                                td.removeClass("value-required");
                                td.addClass("value-not-required");
                            } else {
                                td.removeClass("value-required");
                                td.removeClass("value-not-required");
                            }
                        });

                    }


                });

            });

        </script>

    </body>
</html>
