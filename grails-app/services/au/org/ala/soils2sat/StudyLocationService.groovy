package au.org.ala.soils2sat

class StudyLocationService extends ServiceBase {

    def grailsApplication

    List<StudyLocationSearchResult> getStudyLocations() {
        def studyLocations = proxyServiceCall(grailsApplication, "getStudyLocations")
        def results = new ArrayList<StudyLocationSearchResult>()
        studyLocations?.results?.each { studyLocation ->
            def result = new StudyLocationSearchResult(siteName: studyLocation.siteName, date: studyLocation.date, longitude: studyLocation.longitude, latitude: studyLocation.latitude)
            results.add(result)
        }

        return results
    }

    List<StudyLocationSearchResult> searchStudyLocations(String query, BoundingBox boundingBox) {
        def results = new ArrayList<StudyLocationSearchResult>()
        def studyLocations = proxyServiceCall(grailsApplication, "getStudyLocations")?.results
        def q = query?.toLowerCase()

        if (query == "*") {
            query = ''; // force match all...
        }
        if (studyLocations) {
            studyLocations.each { studyLocation ->
                boolean match = true
                if (query) {
                    if (!studyLocation.siteName?.toLowerCase()?.contains(q)) {
                        match = false
                    }
                }

                if (boundingBox && match) {
                    if (!boundingBox.contains(studyLocation.longitude, studyLocation.latitude)) {
                        match = false
                    }
                }

                if (match) {
                    def result = new StudyLocationSearchResult(siteName: studyLocation.siteName, date: studyLocation.date, longitude: studyLocation.longitude, latitude: studyLocation.latitude)
                    results.add(result)
                }
            }
        }

        return results
    }

    StudyLocationSummary getStudyLocationSummary(String studyLocationName) {
        def studyLocations = proxyServiceCall(grailsApplication, "getStudyLocationSummary", [siteNames: studyLocationName])?.studyLocationSummaryList
        StudyLocationSummary result = null
        if (studyLocations) {
            studyLocations.each { studyLocation ->
                result = new StudyLocationSummary(name: studyLocation.siteLocationName, longitude: studyLocation.longitude, latitude: studyLocation.latitude, data: studyLocation)
            }
        }
        return result
    }

    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.aekosServiceRoot}"
    }
}
