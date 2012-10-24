<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
	<head>
    <r:require module='jqueryui' />
    <r:require module='bootstrap_responsive' />
    <r:require module='fancybox' />
		<meta name="layout" content="detail"/>
		<title>Study Location Summary - ${studyLocationName}</title>
	</head>
	<body>
    <div class="container-fluid">
      <legend>Study Location Summary ${studyLocationName}</legend>
      <div class="well well-small">
        <h4>Study Location Details</h4>
        <table class="table table-bordered table-striped">
          <tr>
            <td>Position</td>
            <td>${studyLocation.longitude}, ${studyLocation.latitude}</td>
          </tr>
          <tr>
            <td>Bioregion Name</td>
            <td>${studyLocation.data.bioregionName}</td>
          </tr>
          <tr>
            <td>siteLocSysId</td>
            <td>${studyLocation.data.siteLocSysId}</td>
          </tr>
          <tr>
            <td>Landform element</td>
            <td>${studyLocation.data.landformElement}</td>
          </tr>
          <tr>
            <td>Landform pattern</td>
            <td>${studyLocation.data.landformPattern}</td>
          </tr>
          <tr>
            <td>Number of distinct plant species (Unverified)</td>
            <td>${studyLocation.data.numDistinctPlantSpeciesUnverified}</td>
          </tr>
          <tr>
            <td>Number of distinct plant species (Verified)</td>
            <td>${studyLocation.data.numDistinctPlantSpeciesVerified}</td>
          </tr>

          <tr>
            <td>Total number of distinct plant species</td>
            <td>${studyLocation.data.numDistinctPlantSpeciesTotal}</td>
          </tr>

          <tr>
            <td>Number of visits</td>
            <td>${studyLocation.data.numVisits}</td>
          </tr>
          <tr>
            <td>First visit date</td>
            <td>${studyLocation.data.firstVisitDate}</td>
          </tr>
          <tr>
            <td>Last visit date</td>
            <td>${studyLocation.data.lastVisitDate}</td>
          </tr>
          <tr>
            <td>Number of sampling units</td>
            <td>${studyLocation.data.numSamplingUnits}</td>
          </tr>
          <tr>
            <td>Sampling unit types</td>
            <td>${studyLocation.data.samplingUnitTypeList?.join(', ')}</td>
          </tr>
        </table>
      </div>
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