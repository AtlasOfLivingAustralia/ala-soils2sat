
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

<html>
    <head>
        <meta name='layout' content='main'/>
        <title>User Registration</title>
        <meta name="layout" content="soils2sat"/>

        <style type='text/css' media='screen'>
        </style>

    </head>

    <body>
        <content tag="topLevelNav">home</content>
        <div id='login' class="container-fluid">

            <div class="hero-unit">
                <h3>User Registration</h3>
                    <g:if test='${flash.message}'>
                    <div class="alert alert-error">
                        <div class='login_message'>${flash.message}</div>
                    </div>
                </g:if>

                <form class="form-horizontal" controller="login" action="registerSubmit" >

                    <div class="control-group">
                        <label class="control-label" for='email'>Email:</label>
                        <div class="controls">
                            <input type='text' id='email' name='email' placeholder="Email" value="${email}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for='password'><g:message code="springSecurity.login.password.label"/>:</label>
                        <div class="controls">
                            <input type='password' name='password' id='password' placeholder="password" value="${password}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for='password2'>Re-enter password:</label>
                        <div class="controls">
                            <input type='password' name='password2' id='password2' placeholder="re-enter password" value="${password2}" } />
                        </div>
                    </div>

                    <div class="control-group">
                        <div class="controls">
                            <input class="btn btn-primary" type='submit' id="submit" value='Register'/>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </body>
</html>