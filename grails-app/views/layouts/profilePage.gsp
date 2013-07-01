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
    <r:require module="bootstrap-responsive-css"/>
    <div class="container-fluid">
        <legend>
            <table style="width: 100%">
                <tr>
                    <td>
                        <sts:homeBreadCrumb />
                        <sts:navSeperator/>
                        User Profile
                        <sts:navSeperator/>
                        <span class="sts-breadcrumb"><g:pageProperty name="page.pageTitle"/></span>
                    </td>
                    <td style="text-align: right"><span><g:pageProperty name="page.adminButtonBar"/></span></td>
                </tr>
            </table>
        </legend>

        <div class="row-fluid">
            <div class="span3">
                <ul class="nav nav-list nav-stacked nav-tabs">
                    <sts:breadcrumbItem href="${createLink(controller: 'userProfile', action: 'index')}" title="Details" />
                    <sts:breadcrumbItem href="${createLink(controller: 'userProfile', action: 'extractions')}" title="My Extractions" active="Extractions" />
                </ul>
            </div>

            <div class="span9">
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