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
                        <h4>Data Extraction Results</h4>
                            Success: ${extractionResults.success}
                            <ul>
                                <li>Link to download (zip file): ${extractionResults.url}</li>
                                <li>DOI</li>
                                <li>RIF/CS Receipt info</li>
                            </ul>
                        </div>

                        <g:link class="btn btn-primary" event="finish">Return to Map</g:link>

                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>
