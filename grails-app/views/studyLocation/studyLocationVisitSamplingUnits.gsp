<!doctype html>
<html>
	<head>
    <r:require module='jqueryui' />
    <r:require module='bootstrap_responsive' />
    <r:require module='fancybox' />
		<meta name="layout" content="detail"/>
		<title>Study Location Visit Summary - ${studyLocationName}</title>
	</head>
	<body>
    <div class="container-fluid">
      <legend>
        <table style="width:100%">
          <tr>
            <td>Study Location&nbsp;&#187;&nbsp;
              <a href="${createLink(controller:'studyLocation', action:'studyLocationSummary', params:[studyLocationName: studyLocationName])}">${studyLocationName}</a>&nbsp;&#187;&nbsp;
              <a href="${createLink(controller:'studyLocation', action:'studyLocationVisitSummary', params:[studyLocationName: studyLocationName])}">Visits</a>&nbsp;&#187;&nbsp;
              Visit ${visit.visitId} Sampling Units</td>
            <td></td>
          </tr>
        </table>
      </legend>
      <div class="well well-small">
        <h4>Study Location Visit Sampling Units</h4>
        <table class="table table-bordered table-striped">
          <g:each in="${visit.samplingUnitSummaryList}" var="su">
            <tr>
              <td>${su.sampleDate}</td>
              <td>${su.observerList?.collect({ it.name })?.join(", ")}</td>
              <td>${su.samplingUnitDescription ?: su.samplingUnit}</td>
            </tr>
          </g:each>
        </table>
      </div>
    </div>
  </body>
</html>