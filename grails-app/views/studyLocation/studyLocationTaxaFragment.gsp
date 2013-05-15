<div class="">
  Taxa list derived from specimen data collected within ${radius} kilometeres of ${studyLocationName} (${studyLocationDetails.longitude}, ${studyLocationDetails.latitude})
  <table class="table table-striped table-bordered">
    <thead>
      <tr>
        <th>${rank}</th>
      </tr>
    </thead>
    <g:each in="${taxaList}" var="taxon">
      <tr>
        <td><sts:taxaHomePageLink name="${taxon}" /></td>
      </tr>
    </g:each>
  </table>
</div>