<div class="">
    <span>
        ${taxaListDescription}
    </span>
    <table class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>Name</th>
            </tr>
        </thead>
        <g:each in="${taxaList}" var="taxon">
            <g:set var="isWeed" value="${weeds?.find { it.toLowerCase().equalsIgnoreCase(taxon.toLowerCase()) } }" />
            <tr class="${isWeed != null ? 'warning' : '' }">
                <td><sts:taxaHomePageLink name="${taxon}"/></td>
            </tr>
        </g:each>
    </table>
</div>