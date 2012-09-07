<html>
    <head>
        <meta name='layout' content='main'/>
        <title><g:message code="springSecurity.login.title"/></title>
        <meta name="layout" content="soils2sat"/>

        <style type='text/css' media='screen'>
        </style>

    </head>

    <body>
        <div id='login' class="container-fluid">

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
    </body>
</html>