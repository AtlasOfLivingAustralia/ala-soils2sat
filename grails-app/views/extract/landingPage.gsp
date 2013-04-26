<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="detail"/>
        <title>Data Extract Landing Page</title>
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
                        <td>Data Extract Landing Page</td>
                        <td></td>
                    </tr>
                </table>
            </legend>

            <g:if test="${extraction}">
                <table class="table table-condensed">
                    <tr>
                        <td><b>Package Name</b></td>
                        <td>${extraction.packageName}</td>
                    </tr>
                    <tr>
                        <td><b>Created By</b></td>
                        <td>${extraction.username}</td>
                    </tr>
                    <tr>
                        <td><b>Created On</b></td>
                        <td><g:formatDate date="${extraction.date}" format="${au.org.ala.soils2sat.DateUtils.S2S_DATE_TIME_FORMAT}" /></td>
                    </tr>
                    <tr>
                        <td><b>Package File</b></td>
                        <td>
                            <g:if test="${filesize > 0}">
                                <a href="${createLink(controller: 'extract', action:'downloadPackage', params:[packageName: extraction.packageName])}">${filename}</a>&nbsp;(${org.apache.commons.io.FileUtils.byteCountToDisplaySize(filesize)})
                            </g:if>
                            <g:else>
                                <span class="error">Download file could not be located!</span>
                            </g:else>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <a class="btn btn-small" href="${createLink(controller: 'extract', action:'downloadPackage', params:[packageName: extraction.packageName])}">
                                <i class="icon-download"></i>&nbsp;Download package
                            </a>
                        </td>
                    </tr>
                </table>
            </g:if>
            <g:else>
            </g:else>

        </div>
    </body>
</html>