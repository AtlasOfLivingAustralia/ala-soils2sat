<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <sec:ifLoggedIn>
            <meta name="layout" content="profilePage"/>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <meta name="layout" content="detail"/>
        </sec:ifNotLoggedIn>
        <title>Data Extract Landing Page</title>
    </head>

    <body>

        <style type="text/css">
        </style>

        <script type="text/javascript">

            $(document).ready(function () {
            });

        </script>

        <sec:ifLoggedIn>
            <content tag="pageTitle">Extract ${extraction.packageName}</content>
        </sec:ifLoggedIn>

        <g:set var="containerClass" value=""/>
        <sec:ifNotLoggedIn>
            <g:set var="containerClass" value="container"/>
        </sec:ifNotLoggedIn>

        <div class="${containerClass}">
            <sec:ifNotLoggedIn>
                <legend>
                    <table style="width:100%">
                        <tr>
                            <td>Data Extract Landing Page - ${extraction.packageName}</td>
                            <td></td>
                        </tr>
                    </table>
                </legend>
            </sec:ifNotLoggedIn>

            <g:if test="${extraction}">
                <table class="table table-condensed table-bordered">
                    <tr>
                        <td><b>Package Name</b></td>
                        <td>${extraction.packageName}</td>
                    </tr>
                    <tr>
                        <td><b>Created By</b></td>
                        <td>${author?.userProfile?.fullName ?: extraction?.username}</td>
                    </tr>
                    <tr>
                        <td><b>Created On</b></td>
                        <td><g:formatDate date="${extraction.date}" format="${au.org.ala.soils2sat.DateUtils.S2S_DATE_TIME_FORMAT}" /></td>
                    </tr>
                    <tr>
                        <td><b>DOI</b></td>
                        <td>${extraction.doi}</td>
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
                        <td></td>
                        <td>
                            <a class="btn btn-small" href="${createLink(controller: 'extract', action:'downloadPackage', params:[packageName: extraction.packageName])}">
                                <i class="icon-download"></i>&nbsp;Download package
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td><strong>Package&nbsp;Manifest</strong></td>
                        <td>
                            <g:set var="lines" value="${manifestText?.split("\n")}" />
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Filename</th>
                                        <th>Description</th>
                                    </tr>
                                </thead>
                                <g:each var="line" in="${lines}">
                                    <g:set var="bits" value="${line.split('\t')}"/>
                                    <tr>
                                        <td>
                                            ${bits[0]}
                                        </td>
                                        <td>
                                            ${bits[1]}
                                        </td>
                                    </tr>
                                </g:each>
                            </table>
                            <code>

                            </code>
                        </td>
                    </tr>
                </table>

            </g:if>
            <g:else>
            </g:else>

        </div>
    </body>
</html>