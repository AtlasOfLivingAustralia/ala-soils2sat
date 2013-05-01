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
                        <h4>Sampling Units</h4>
                            <table>
                                <tr>
                                    <td style="width: 30px">
                                        <g:radio style="display: inline-block; vertical-align: middle; margin-top: 0px" name="samplingUnits" value="all" checked="true"/>
                                    </td>
                                    <td>
                                        <span>All available sampling units</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <g:radio style="display: inline-block; vertical-align: middle; margin-top: 0px" disabled="true" name="samplingUnits" value="selected"/>
                                    </td>
                                    <td>
                                        <span class="muted">Selected sampling units</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <g:radio style="display: inline-block; vertical-align: middle; margin-top: 0px" disabled="true" name="samplingUnits" value="matrix"/>
                                    </td>
                                    <td>
                                        <span class="muted">Answer specific question...</span>
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <g:link class="btn btn-small" event="cancel">Cancel</g:link>
                        <g:link class="btn btn-small" event="back"><i class="icon-chevron-left"></i>&nbsp;Previous</g:link>
                        <g:link class="btn btn-small btn-primary" event="continue">Next&nbsp;<i class="icon-chevron-right icon-white"></i></g:link>

                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>
