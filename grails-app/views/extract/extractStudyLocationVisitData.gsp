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

        <div class="container-fluid">
            <legend>
                <table style="width:100%">
                    <tr>
                        <td>
                            <sts:homeBreadCrumb />
                            <sts:navSeperator/>
                            <span class="sts-breadcrumb">Extract Data from selected Study Location Visits</span>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </legend>

            <div class="well well-small">
                <h4>Selected Study Location Visits</h4>
                <table>
                    <g:each in="${appState.selectedVisits}" var="visit">
                        <tr>
                            <td style="width:30px; vertical-align: middle"><g:checkBox style="display: inline-block; vertical-align: middle; margin-top:0px" name="visitId" checked="true" value="${visit.studyLocationVisitId}" /></td>
                            <td>${visit.studyLocationName}<sts:navSeperator/>${visit.studyLocationVisitId} <sts:formatVisitLabel studyLocationName="${visit.studyLocationName}" studyLocationVisitId="${visit.studyLocationVisitId}" /> </td>
                        </tr>
                    </g:each>
                </table>

            </div>
        </div>
    </body>
</html>