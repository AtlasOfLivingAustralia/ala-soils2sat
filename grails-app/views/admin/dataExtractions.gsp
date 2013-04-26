<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Data Extractions</title>
    </head>

    <body>

        <script type="text/javascript">

            $(document).ready(function() {
                $(".btnDelete").click(function(e) {
                    e.preventDefault();
                    var packageName = $(this).parents("[packageName]").attr("packageName");
                    if (packageName) {
                        if (confirm("Warning !!! If you delete this package you will break any published DOI links to it. This should only be done if you are sure that it is safe to do so. Delete package '" + packageName + "' ?")) {
                            window.location = "${createLink(controller: 'admin', action:'deleteDataExtraction')}?packageName=" + packageName
                        }
                    }
                });
            });

        </script>

        <content tag="pageTitle">Data Extractions</content>

        <content tag="adminButtonBar">
        </content>

        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <g:sortableColumn title="Date" property="date" />
                    <g:sortableColumn title="Package Name" property="packageName" />
                    <g:sortableColumn title="Username" property="username" />
                    <g:sortableColumn title="Downloads" property="downloadCount" />
                    <th>Landing page</th>
                    <th/>
                </tr>
            </thead>
            <tbody>
                <g:each in="${extractions}" var="extraction">
                    <tr packageName="${extraction.packageName}">
                        <td><g:formatDate date="${extraction.date}" format="${au.org.ala.soils2sat.DateUtils.S2S_DATE_TIME_FORMAT}" /> </td>
                        <td>${extraction.packageName}</td>
                        <td>${extraction.username}</td>
                        <td>${extraction.downloadCount}</td>
                        <td><a href="${createLink(controller:'extract', action:'landingPage', params:[packageName: extraction.packageName])}">Landing page</a></td>
                        <td>
                            <a class="btn btn-mini" href="${createLink(controller: 'extract',action:'downloadPackage', params:[packageName: extraction.packageName])}">Download</a>
                            <button class="btn btn-mini btn-danger btnDelete" title="Delete package"><i class="icon-remove icon-white"></i></button>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>
        <div class="pagination">
            <g:paginate total="${extractions.totalCount}" omitNext="true" omitPrev="true" />
        </div>
    </body>

</html>