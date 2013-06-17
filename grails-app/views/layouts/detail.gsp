<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title><g:layoutTitle default="Soils2Sat"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="https://www.ala.org.au/wp-content/themes/ala2011/images/favicon.ico"
              type="image/x-icon">
        <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
        <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'soils2sat.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: '/jqwidgets/styles', file: 'jqx.base.css')}" type="text/css"/>

        <g:javascript library="application"/>
        <r:require module='jqueryui' />
        <r:require module="bootstrap"/>
        <r:layoutResources />

        <script type="text/javascript" src="http://www.google.com/jsapi"></script>

        <Style type="text/css">

        body {
            padding-top: 60px;
        }

        </Style>
        <script type="text/javascript">

            $(document).ready(function (e) {

                $.ajaxSetup({ cache: false });

                $("#btnLogout").click(function (e) {
                    window.location = "${createLink(controller: 'logout', action:'index')}";
                });

                $("#btnAdministration").click(function (e) {
                    window.location = "${createLink(controller: 'admin')}";
                });

                $("#btnProfile").click(function (e) {
                    window.location = "${createLink(controller: 'userProfile')}";
                });

            });

            function displayLayerInfo(layerName) {

                showModal({
                    url: "${createLink(controller: 'map', action:'layerInfoFragment', params:[dummy:1])}&layerName=" + layerName,
                    title: "Layer details - " + layerName,
                    height: 520,
                    width: 800
                });
                return true;
            }

        </script>

    </head>

    <body style="overflow: auto">
        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">

                <div class="container-fluid">
                    <a class="brand" href="${createLink(controller: 'map', action: 'index')}" style="padding-bottom: 0; padding-top: 0">
                        <img src="${resource(dir:'/images', file:'Soils-to-Satellites_40px.png')}" />
                    </a>
                    <div class="nav-collapse collapse">
                        <div class="navbar-text pull-right">
                            <span id="buttonBar">
                                <sec:ifLoggedIn>
                                    <sec:username/>&nbsp;<button class="btn btn-small" id="btnLogout"><i class="icon-off"></i>&nbsp;Logout</button>
                                    <button class="btn btn-small btn-info" id="btnProfile"><i class="icon-user icon-white"></i>&nbsp;My Profile</button>
                                </sec:ifLoggedIn>
                                <sts:ifAdmin>
                                  <button class="btn btn-warning btn-small" id="btnAdministration">Administration</button>
                                </sts:ifAdmin>
                                <g:pageProperty name="page.buttonBar"/>
                            </span>
                        </div>
                        <sts:navbar active="${pageProperty(name: 'page.topLevelNav')}" />
                    </div><!--/.nav-collapse -->
                </div>
            </div>
        </div>
        <g:if test="${message ?: flash.message}">
            <div class="container">
                <div class="row">
                    <div class="span12">
                        <div class="alert alert-info">
                            <button type="button" class="close" data-dismiss="alert">×</button>
                            ${message ?: flash.message}
                        </div>
                    </div>
                </div>
            </div>
        </g:if>

        <g:if test="${errorMessage ?: flash.errorMessage}">
            <div class="container">
                <div class="row">
                    <div class="span12">
                        <div class="alert alert-error">
                            ${errorMessage ?: flash.errorMessage}
                        </div>
                    </div>
                </div>
            </div>
        </g:if>

        <g:layoutBody />
        <r:layoutResources />
        <div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
    </body>
</html>