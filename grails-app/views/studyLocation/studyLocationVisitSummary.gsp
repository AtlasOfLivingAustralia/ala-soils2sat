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
            <td>Study Location Visit Summary for <a href="${createLink(controller:'studyLocation', action:'studyLocationSummary', params:[studyLocationName: studyLocationName])}">${studyLocationName}</a></td>
            <td></td>
          </tr>
        </table>
      </legend>
      <div class="well well-small">
        <h4>Study Location Visits</h4>
        <table class="table table-bordered table-striped">
        <g:each in="${studyLocation.data.visitSummaryList}" var="visit">
          <tr>
            <td>${visit.startDate ?: studyLocation.data.firstVisitDate} - ${visit.endDate ?: studyLocation.data.lastVisitDate}</td>
            <td>${visit.observerList?.collect({ it.name })?.join(", ")}</td>
            <td>${visit.samplingUnitNameList?.join(", ")}</td>
          </tr>
        </g:each>
        </table>
      </div>
    </div>
  </body>
</html>
