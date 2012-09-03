<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="https://www.ala.org.au/wp-content/themes/ala2011/images/favicon.ico" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'soils2sat.css')}" type="text/css">
		<g:layoutHead/>
		<r:layoutResources />
    <Style type="text/css">
      #header {
        width: 100%;
        background: #DF4A21;
      }
      html, body {
        margin: 0px;
        width: 100%;
        height: 100%;
      }

      .footer {
        height: 90px;
        color: white;
        bottom: 0px;
        position: absolute;
        width: 100%;
        background-color: #efefef;

      }
    </Style>
	</head>
	<body>
		<div id="header"><a href="${createLink(uri: '/index.gsp')}"><img src="${resource(dir: 'images', file: 'logo.png')}" alt="Soils to Satellites"/></a>
    </div>
		<g:layoutBody/>
		<div class="footer" role="contentinfo">
      <img src="${resource(dir:'/images', file:'S2S-banner.png')}" height="80px" />
		</div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
		<g:javascript library="application"/>
		<r:layoutResources />
	</body>
</html>