<!doctype html>
<html>
	<head>
    <r:require module='jqueryui' />
    <r:require module='bootstrap_responsive' />
    <r:require module='fancybox' />
		<meta name="layout" content="admin"/>
		<title>Soils to Satellites - Admin - User List</title>
	</head>
	<body>
      <script type='text/javascript'>

        $(document).ready(function() {
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
                          <g:textField id="email" name="email" placeholder="Email" value="${params.email}" />
                          %{--<g:if test="${!userInstance}">--}%
                            %{--<g:textField name="email" placeholder="Email" value="${params.email}" />--}%
                          %{--</g:if>--}%
                          %{--<g:else>--}%
                            %{--<span>${userInstance.username}</span>--}%
                          %{--</g:else>--}%
                      </div>
                  </div>

                  <div class="control-group">
                      <label class="control-label" for='password'><g:message code="springSecurity.login.password.label"/>:</label>
                      <div class="controls">
                          <g:passwordField name='password' id='password' placeholder="password" value="${params.password}"/>
                      </div>
                  </div>

                  <div class="control-group">
                      <label class="control-label" for='password2'>Re-enter password:</label>
                      <div class="controls">
                          <g:passwordField name='password2' id='password2' placeholder="re-enter password" value="${params.password2}" />
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
                          <g:submitButton class="btn btn-primary" name="submit" value='${userInstance ? "Save": "Add user"}' />
                      </div>
                  </div>

                  <g:hiddenField name="userId" value="${userInstance?.id}" />

              </g:form>
          </div>
      </div>
  </body>
</html>