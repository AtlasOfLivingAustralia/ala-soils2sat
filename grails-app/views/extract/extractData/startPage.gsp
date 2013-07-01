<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
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
                        <td>
                            <sts:homeBreadCrumb />
                            <sts:navSeperator/>
                            <span class="sts-breadcrumb">Extract Data from selected Study Location Visits</span>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </legend>

            <div class="row">
                <div class="span12">
                    <div class="well well-small">
                        <h4>Extract Data</h4>
                        Some explanatory text that outlines what is going to happen, including:
                        <ul>
                            <li>Confirming/selecting which visits to export</li>
                            <li>Confirming/selecting which sampling units to export</li>
                            <li>The minting of a DOI</li>
                            <li>The publication of a RIF/CS thing</li>
                            <li>The actual extraction and packaging of the data</li>
                        </ul>
                    </div>
                    <g:link class="btn btn-small" event="cancel">Cancel</g:link>
                    <g:link class="btn btn-small btn-primary" event="continue">Next&nbsp;<i class="icon-chevron-right icon-white"></i></g:link>
                </div>
            </div>
        </div>
    </body>
</html>
