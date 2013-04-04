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
                        <td><a href="${createLink(controller:'map', action:'index')}">Map</a>&nbsp;&#187;&nbsp;Extract Data from selected Study Location Visits</td>
                        <td>
                        </td>
                    </tr>
                </table>
            </legend>

            <div class="well well-small">
            </div>
        </div>
    </body>
</html>