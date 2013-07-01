<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Settings</title>
    </head>

    <body>
        <script type="text/javascript">

            $(document).ready(function() {
            });

        </script>
        <content tag="pageTitle">Settings</content>
        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th>Setting</th>
                    <th>Value</th>
                    <th>Comment</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <g:each var="setting" in="${settings}">
                    <tr>
                        <td>
                            ${setting.key}
                        </td>
                        <td>
                            ${setting.value}
                        </td>
                        <td>
                            ${setting.comment}
                        </td>
                        <td>
                            <a href="${createLink(controller:'admin', action:'editSetting', id: setting.id)}" title="Edit this setting">
                                <i class="icon-edit"></i>
                            </a>
                        </td>
                    </tr>
                </g:each>

            </tbody>
        </table>
    </body>
</html>