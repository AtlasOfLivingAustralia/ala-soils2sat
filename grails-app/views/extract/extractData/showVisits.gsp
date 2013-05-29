<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="detail"/>
        <title>Extract Data from Study Location Visits</title>
    </head>

    <body>

        <style type="text/css">
        </style>

        <script type="text/javascript">

            $(document).ready(function () {
            });

        </script>

        <div class="container">
            <legend>
                <table style="width:100%">
                    <tr>
                        <td><a href="${createLink(controller:'map', action:'index')}">Map</a><sts:navSeperator/>Extract Data from selected Study Location Visits</td>
                        <td>
                        </td>
                    </tr>
                </table>
            </legend>

            <div class="row">
                <div class="span12">
                    <g:form>
                        <div class="well well-small">
                            <h4>Selected Study Location Visits</h4>
                            <table>
                                <g:each in="${appState.selectedVisits}" var="visit">
                                    <tr>
                                        <g:set var="isChecked" value="${selectedVisitIds?.contains(visit.studyLocationVisitId)}" />
                                        <td style="width:30px; vertical-align: middle"><g:checkBox style="display: inline-block; vertical-align: middle; margin-top:0" name="visitId" checked="${isChecked}" value="${visit.studyLocationVisitId}" /></td>
                                        <td>${visit.studyLocationName}<sts:navSeperator/>${visit.studyLocationVisitId} <sts:formatVisitLabel studyLocationName="${visit.studyLocationName}" studyLocationVisitId="${visit.studyLocationVisitId}" /> </td>
                                    </tr>
                                </g:each>
                            </table>

                        </div>
                        <g:link class="btn btn-small" event="cancel">Cancel</g:link>
                        <g:link class="btn btn-small" event="back"><i class="icon-chevron-left"></i>&nbsp;Previous</g:link>
                        <g:submitButton name="continue" class="btn btn-small btn-primary" value="Next" />
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>