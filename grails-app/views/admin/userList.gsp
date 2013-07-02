%{--
- ﻿Copyright (C) 2013 Atlas of Living Australia
- All Rights Reserved.
-
- The contents of this file are subject to the Mozilla Public
- License Version 1.1 (the "License"); you may not use this file
- except in compliance with the License. You may obtain a copy of
- the License at http://www.mozilla.org/MPL/
-
- Software distributed under the License is distributed on an "AS
- IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
- implied. See the License for the specific language governing
- rights and limitations under the License.
--}%
<!doctype html>
<html>
    <head>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - User List</title>
    </head>

    <body>

        <content tag="pageTitle">Users</content>
        <table class="table table-condensed table-striped table-bordered">
            <thead>
                <tr>
                    <th>Username</th>
                    <th>Roles</th>
                    <th>Last activity</th>
                    <th>Actions</th>
                </tr>
            </thead>

            <g:each in="${userList}" var="user">
                <tr userId="${user.id}">
                    <td>${user.username}</td>
                    <td><sts:roles user="${user}"/></td>
                    <td>${user.applicationState?.lastLogin ? formatDate(format: "yyyy-MM-dd HH:mm:ss", date: user.applicationState.lastLogin) : 'N/A'}</td>
                    <td>
                        <button class="btn btn-danger btn-mini btnDeleteUser"><i class="icon-remove icon-white"></i>&nbsp;delete</button>
                        <button class="btn btn-mini btnEditUser"><i class="icon-edit"></i>&nbsp;edit</button>
                    </td>
                </tr>
            </g:each>
        </table>

        <g:paginate total="${totalUsers}" controller="admin" action="userList"/>

        <content tag="adminButtonBar">
            <button class="btn btn-small btn-primary" id="btnAddNewUser"><i class="icon-user icon-white"></i>&nbsp;Add user</button>
        </content>

        <script type="text/javascript">
            $(document).ready(function (e) {

                $("#btnAddNewUser").click(function (e) {
                    window.location = "${createLink(controller:'admin', action: 'addUser')}"
                });

                $(".btnEditUser").click(function (e) {
                    var userId = $(this).parents("tr[userId]").attr("userId");
                    if (userId) {
                        window.location = "${createLink(controller:'admin', action: 'editUser')}/" + userId
                    }
                });

                $(".btnDeleteUser").click(function (e) {
                    var userId = $(this).parents("tr[userId]").attr("userId");
                    if (userId) {
                        if (confirm("Are you sure you wish to delete user " + userId + "?")) {
                            window.location = "${createLink(controller:'admin', action:'deleteUser')}/" + userId;
                        }
                    }
                });

            });
        </script>

    </body>
</html>
