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

    <g:set var="visitSummaryLink" value="${createLink(controller:'studyLocation', action: 'studyLocationVisitSummary', params:[studyLocationName: studyLocationName])}" />

    <style type="text/css">

      .tab-content {
        border-left: 1px solid #d3d3d3;
        border-right: 1px solid #d3d3d3;
        border-bottom: 1px solid #d3d3d3;
        padding: 10px;
        background-color: white;
      }

    </style>

    <script type="text/javascript">

      $(document).ready(function() {

        $('a[data-toggle="tab"]').on('shown', function (e) {
          $("#environmentalLayersTab").html("");
          var tabHref = $(this).attr('href');
          if (tabHref == '#environmentalLayersTab') {
            $("#environmentalLayersTab").html('Retrieving data for study location... <img src="${resource(dir:'/images', file:'spinner.gif')}"/></div>');
            $.ajax("${createLink(controller: 'studyLocation', action: 'studyLocationLayersFragment', params: [studyLocationName: studyLocationName])}").done(function(html) {
              $("#environmentalLayersTab").html(html);
            });
          } else if (tabHref == "#taxaTab") {
            $("#taxaTab").html('Retrieving taxa data for study location... <img src="${resource(dir:'/images', file:'spinner.gif')}"/></div>');
            $.ajax("${createLink(controller: 'studyLocation', action: 'studyLocationTaxaFragment', params: [studyLocationName: studyLocationName])}").done(function(html) {
              $("#taxaTab").html(html);
            });
          }
        });

        $("#btnViewVisitSummaries").click(function(e) {
          e.preventDefault();
          window.location = "${visitSummaryLink}";
        })

      });

    </script>
    <div class="container-fluid">
      <legend>
        <table style="width:100%">
          <tr>
            <td>Study Location Summary ${studyLocationName}</td>
            <td><button id="btnViewVisitSummaries" class="btn btn-small pull-right">View Visit Summaries (${studyLocationSummary.data.numVisits})</button></td>
          </tr>
        </table>
      </legend>
      <div class="well well-small">

        <div class="tabbable">

          <ul class="nav nav-tabs" style="margin-bottom: 0px">
            <li class="active"><a href="#detailsTab" data-toggle="tab">Details</a></li>
            <li><a href="#environmentalLayersTab" data-toggle="tab">Environmental data</a></li>
            <li><a href="#taxaTab" data-toggle="tab">Taxa data</a></li>
          </ul>

          <div class="tab-content" >
            <div class="tab-pane active" id="detailsTab">
              <h4>Study Location Details</h4>
              <table class="table table-bordered table-striped">
                <tr>
                  <td>Position</td>
                  <td>${studyLocationSummary.longitude}, ${studyLocationSummary.latitude}</td>
                </tr>
                <tr>
                  <td>Bioregion Name</td>
                  <td>${studyLocationSummary.data.bioregionName}</td>
                </tr>
                <tr>
                  <td>siteLocSysId</td>
                  <td>${studyLocationSummary.data.siteLocSysId}</td>
                </tr>
                <tr>
                  <td>Landform element</td>
                  <td>${studyLocationSummary.data.landformElement}</td>
                </tr>
                <tr>
                  <td>Landform pattern</td>
                  <td>${studyLocationSummary.data.landformPattern}</td>
                </tr>
                <tr>
                  <td>Number of distinct plant species (Unverified)</td>
                  <td>${studyLocationSummary.data.numDistinctPlantSpeciesUnverified}</td>
                </tr>
                <tr>
                  <td>Number of distinct plant species (Verified)</td>
                  <td>${studyLocationSummary.data.numDistinctPlantSpeciesVerified}</td>
                </tr>

                <tr>
                  <td>Total number of distinct plant species</td>
                  <td>${studyLocationSummary.data.numDistinctPlantSpeciesTotal}</td>
                </tr>

                <tr>
                  <td>Number of visits</td>
                  <td><a href="${visitSummaryLink}">${studyLocationSummary.data.numVisits}</a></td>
                </tr>
                <tr>
                  <td>First visit date</td>
                  <td>${studyLocationSummary.data.firstVisitDate}</td>
                </tr>
                <tr>
                  <td>Last visit date</td>
                  <td>${studyLocationSummary.data.lastVisitDate}</td>
                </tr>
                <tr>
                  <td>Number of sampling units</td>
                  <td>${studyLocationSummary.data.numSamplingUnits}</td>
                </tr>
                <tr>
                  <td>Sampling unit types</td>
                  <td>${studyLocationSummary.data.samplingUnitTypeList?.join(', ')}</td>
                </tr>
              </table>
            </div>
            <div class="tab-pane" id="environmentalLayersTab">
            </div>
            <div class="tab-pane" id="taxaTab">
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>