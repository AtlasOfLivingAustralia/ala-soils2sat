package au.org.ala.soils2sat

class BiocacheService extends ServiceBase {

    def grailsApplication
    def settingService

    List<String> getTaxaNamesForLocation(double latitude, double longitude) {

        def rank = settingService.observationsRank

        def data = proxyServiceCall(grailsApplication, "occurrences/spatial", [lat: latitude, lon: longitude, radius: settingService.observationRadius, pageSize: 0, facets: rank, fq:settingService.taxonFilter])

        def results = new ArrayList<>()

        def rankResults = data?.facetResults?.find { it.fieldName == rank }
        if (rankResults) {
            rankResults.fieldResult?.each {
                results << it.label
            }
        }

        return results?.sort { it }
    }

    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.biocacheServiceRoot}"
    }

    List<String> getWeedsOfNationalSignificance() {
        // http://lists.ala.org.au/ws/speciesListItems/dr823
        def list = proxyServiceCall(grailsApplication, '', [serviceUrl:'http://lists.ala.org.au/ws/speciesListItems/dr823'])
        def results = list.collect { it.name }
        return results
    }

}
