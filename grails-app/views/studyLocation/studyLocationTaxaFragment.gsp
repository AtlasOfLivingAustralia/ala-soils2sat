<div class="">
    Vouchered specimens recorded at ${studyLocationDetails.studyLocationName}
    <table class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>Species</th>
            </tr>
        </thead>
        <g:each in="${ausplotsNames}" var="taxon">
            <g:set var="isWeed" value="${weeds.find { it.toLowerCase().equalsIgnoreCase(taxon.toLowerCase()) } }" />
            <tr class="${isWeed != null ? 'warning' : '' }">
                <td><sts:taxaHomePageLink name="${taxon}"/></td>
            </tr>
        </g:each>
    </table>

    Taxa list derived from specimen data collected within ${radius} kilometeres of ${studyLocationDetails?.studyLocationName} (${studyLocationDetails.longitude}, ${studyLocationDetails.latitude})
    <table class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>${rank}</th>
            </tr>
        </thead>
        <g:each in="${alaNames}" var="taxon">
            <g:set var="isWeed" value="${weeds.find { it.toLowerCase().equalsIgnoreCase(taxon.toLowerCase()) } }" />
            <tr class="${isWeed != null ? 'warning' : '' }">
                <td><sts:taxaHomePageLink name="${taxon}"/></td>
            </tr>
        </g:each>
    </table>
</div>