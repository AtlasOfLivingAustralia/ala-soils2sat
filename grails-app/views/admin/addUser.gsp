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
        <script type='text/javascript'>

            $(document).ready(function () {
                <g:if test="${userInstance}">
                $("#email").attr("disabled", "disabled");
                </g:if>
            });
        </script>

        <div id='login' class="container-fluid">

            <div class="hero-unit">
                <h3>${userInstance ? 'Edit User' : 'New User registration'}</h3>

                <g:if test='${flash.message}'>
                    <div class="alert alert-error">
                        <div class='login_message'>${flash.message}</div>
                    </div>
                </g:if>

                <g:form class="form-horizontal" controller="admin" action="saveUser" autocomplete="off">

                    <div class="control-group">
                        <label class="control-label" for='email'>Email:</label>

                        <div class="controls">
                            <g:textField id="email" name="email" placeholder="Email" value="${params.email}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for='password'><g:message
                            code="springSecurity.login.password.label"/>:</label>

                        <div class="controls">
                            <g:passwordField name='password' id='password' placeholder="password"
                                             value="${params.password}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for='password2'>Re-enter password:</label>

                        <div class="controls">
                            <g:passwordField name='password2' id='password2' placeholder="re-enter password"
                                             value="${params.password2}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for='isAdmin'>User is an administrator</label>

                        <div class="controls">
                            <g:checkBox name="isAdmin" id="isAdmin" checked="${params.isAdmin}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <div class="controls">
                            <g:submitButton class="btn btn-primary" name="submit" value='${userInstance ? "Save" : "Add user"}'/>
                            <a href="${createLink(controller:'userProfile', action:'edit', params:[userId:userInstance?.id])}" class="btn">Edit Citation Profile</a>
                        </div>
                    </div>

                    <g:hiddenField name="userId" value="${userInstance?.id}"/>

                </g:form>
            </div>
        </div>
    </body>
</html>