<html>
    <head>
        <meta name='layout' content='main'/>
        <title><g:message code="springSecurity.login.title"/></title>
        <meta name="layout" content="soils2sat"/>

        <style type='text/css' media='screen'>
        </style>

    </head>

    <body>
        <content tag="topLevelNav">home</content>

        <div id='login' class="container-fluid">
            <div class="row-fluid">
                <div class="hero-unit">
                    <h3>Please Login</h3>

                    <g:if test='${flash.message}'>
                        <div class="alert alert-error">
                            <div class='login_message'>${flash.message}</div>
                        </div>
                    </g:if>

                    <form class="form-horizontal" action='${postUrl}' method='POST' id='loginForm' autocomplete='off'>
                        <div class="control-group">
                            <label class="control-label" for='username'>Username:</label>

                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on"><i class="icon-envelope"></i></span>
                                    <input type='text' name='j_username' id='username' placeholder="Email" value="${username}"/>
                                </div>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for='password'>Password:</label>

                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on"><i class="icon-lock"></i></span>
                                    <input type='password' name='j_password' id='password' placeholder="password" value="${password}"/>
                                </div>
                            </div>
                        </div>

                        %{--<div class="control-group">--}%
                        %{--<div class="controls">--}%
                        %{--<label class="checkbox">--}%
                        %{--<input type="checkbox" name="${rememberMeParameter}" id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>>--}%
                        %{--<g:message code="springSecurity.login.remember.me.label"/>--}%
                        %{--</label>--}%
                        %{--</div>--}%
                        %{--</div>--}%

                        <div class="control-group">
                            <div class="controls">
                                <input class="btn btn-primary" type='submit' id="submit" value='Login'/>
                            </div>
                        </div>

                        <div>
                            Not registered? Click <a href="${createLink(controller: 'login', action: 'register')}">here.</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script type='text/javascript'>
            <!--
            (function () {
                document.forms['loginForm'].elements['j_username'].focus();
            })();
            // -->
        </script>
    </body>
</html>
