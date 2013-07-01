<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
    <head>
        <meta name="layout" content="detail"/>
        <title>Questions - Ecological Contexts</title>
    </head>
    <body>
        <div class="container">

            <legend>
                <table style="width:100%">
                    <tr>
                        <td>Select ecological context(s)</td>
                        <td style="text-align: right">
                            <a class="btn btn-small" href="${createLink(controller:'question', action:'index')}">Go back to questions</a>
                        </td>
                    </tr>
                </table>
            </legend>

            <div class="alert alert-info">
                <div>
                <strong>Selected study locations:</strong>&nbsp;${userInstance.applicationState.selectedPlots.collect({ it.name }).join(", ")}
                </div>
                <div>
                    <strong>Question:</strong>&nbsp;${question.text}
                </div>
            </div>

            <g:form action="showSamplingUnits" controller="question">

                <g:hiddenField name="questionId" value="${question.id}" />

                <div class="well well-small">
                    <h4>Relevant Ecological Contexts:</h4>

                    <g:each in="${contexts}" var="context">
                        <div>
                            <g:checkBox name="context_${context.id}" />
                            <label style="display: inline-block;" for="context_${context.id}">${context.name}</label>
                        </div>
                    </g:each>
                </div>

                <div class="control-group">
                    <div class="controls">
                        <button id="btnBack" class="btn">Back</button>
                        <g:submitButton class="btn btn-primary" name="submit" value='Next'/>
                    </div>
                </div>

            </g:form>

        </div>
        <script type="text/javascript">

            $("#btnBack").click(function(e) {
                e.preventDefault();
                window.location = "${createLink(controller:'question', action:'index')}";
            });

        </script>
    </body>
</html>