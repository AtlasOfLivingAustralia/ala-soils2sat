package au.org.ala.soils2sat

class BiocacheService extends ServiceBase {

    def grailsApplication

    List<String> getTaxaNamesForLocation(double latitude, double longitude, double radius, String rank) {

        def data = proxyServiceCall(grailsApplication, "occurrences/spatial", [lat: latitude, lon: longitude, radius: radius, pageSize: 0, facets: rank])

        def results = new ArrayList<>()

        def rankResults = data?.facetResults?.find { it.fieldName == rank }
        if (rankResults) {
            rankResults.fieldResult?.each {
                results << it.label
            }
        }

        return results
    }

    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.biocacheServiceRoot}"
    }

}
