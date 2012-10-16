<g:applyLayout name="soils2sat">
  <head>
    <style type="text/css">

      .icon-chevron-right {
        float: right;
        margin-top: 2px;
        margin-right: -6px;
        opacity: .25;
      }

    </style>
  </head>

  <body>
    <r:require module="bootstrap_responsive" />
    <div class="container-fluid">
      <legend>
        <table style="width: 100%">
          <tr>
            <td>Administration <g:pageProperty name="page.pageTitle"/></td>
            <td style="text-align: right"><span><g:pageProperty name="page.adminButtonBar" /></span></td>
          </tr>
        </table>
      </legend>
      <div class="row">
        <div class="span4">
            <ul class="nav nav-list nav-stacked nav-tabs">
              <li ${pageProperty(name:'page.pageTitle') == 'Users' ? 'class="active"' : ''}><a href="${createLink(controller:'admin', action:'userList')}"><i class="icon-chevron-right"></i>Users</a></li>
              <li ${pageProperty(name:'page.pageTitle') == 'Layer Sets' ? 'class="active"' : ''}><a href="${createLink(controller:'admin', action:'layerSets')}"><i class="icon-chevron-right"></i>Layer Sets</a></li>
            </ul>
        </div>
        <div class="span8">
          <g:if test="${flash.errorMessage}">
            <div class="container-fluid">
              <div class="alert alert-error">
                ${flash.errorMessage}
              </div>
            </div>
          </g:if>

          <g:if test="${flash.message}">
            <div class="container-fluid">
              <div class="alert alert-info">
                ${flash.message}
              </div>
            </div>
          </g:if>

          <g:layoutBody/>

        </div>
      </div>
    </div>
  </body>
</g:applyLayout>