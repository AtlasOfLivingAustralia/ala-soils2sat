package au.org.ala.soils2sat

import org.codehaus.groovy.grails.web.json.JSONElement

class StudyLocationService extends ServiceBase {

    def grailsApplication
    def layerService

    List<StudyLocationSearchResult> getStudyLocations() {
        def studyLocations = proxyServiceCall(grailsApplication, "getStudyLocations")
        def results = new ArrayList<StudyLocationSearchResult>()
        studyLocations?.results?.each { studyLocation ->
            def result = new StudyLocationSearchResult(siteName: studyLocation.siteName, date: studyLocation.date, longitude: studyLocation.longitude, latitude: studyLocation.latitude)
            results.add(result)
        }

        return results
    }

    List<StudyLocationSearchResult> searchStudyLocations(UserSearch userSearch) {
        def results = new ArrayList<StudyLocationSearchResult>()
        def q = userSearch.searchText?.toLowerCase()

        if (q == "*") {
            q = ''; // force match all...
        }
        BoundingBox boundingBox = null
        if (userSearch.useBoundingBox) {
            boundingBox = new BoundingBox(top: userSearch.top, left:  userSearch.left, bottom: userSearch.bottom, right:  userSearch.right)
        }

        // below should be replaced by a call to the search service, when it exists.
        def studyLocations = proxyServiceCall(grailsApplication, "getStudyLocations")?.results
        if (studyLocations) {
            studyLocations.each { studyLocation ->
                boolean match = true
                if (q) {
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
                    def result = new StudyLocationSearchResult(siteName: studyLocation.siteName, date: studyLocation.date, longitude: studyLocation.longitude, latitude: studyLocation.latitude, easting: studyLocation.easting, northing: studyLocation.northing, zone: studyLocation.zone as Integer)
                    results.add(result)
                }
            }
        }

        // Now we need to post process the search results and filter out that do not match any specified 'ALA' criteria
        if (userSearch.criteria) {
            List<StudyLocationSearchResult> finalResults = new ArrayList<StudyLocationSearchResult>(results)
            results.each { candidate ->
                for (SearchCriteria criteria : userSearch.criteria) {
                    if (!testCriteria(criteria, candidate)) {
                        finalResults.remove(candidate)
                        break;
                    }
                }
            }
            results = finalResults
        }

        return results
    }

    private boolean testCriteria(SearchCriteria criteria, StudyLocationSearchResult candidate) {
        boolean result = false;
        switch (criteria.criteriaDefinition.type) {
            case CriteriaType.SpatialPortalLayer:
            case CriteriaType.SpatialPortalField:
                def value = layerService.getIntersectValues(candidate.latitude, candidate.longitude, [criteria.criteriaDefinition.fieldName])[criteria.criteriaDefinition.fieldName]
                result = SearchCriteriaUtils.eval(criteria, value as String);
                break;
            default:
                // Don't care about other types of criteria, let them through!
                result = true;
                break;
        }
        return result
    }

    StudyLocationSummary getStudyLocationSummary(String studyLocationName) {
        def studyLocations = proxyServiceCall(grailsApplication, "getStudyLocationSummary", [siteNames: studyLocationName])?.studyLocationSummaryList
        StudyLocationSummary result = null
        if (studyLocations) {
            def seqNo = 1
            studyLocations.each { studyLocation ->
                // Hopefully this is just temporary, but there is no unique id for visits in the dummy data, so we'll created one
                studyLocation.visitSummaryList?.each {
                    it.visitId = seqNo++
                }

                result = new StudyLocationSummary(name: studyLocation.siteLocationName, longitude: studyLocation.longitude, latitude: studyLocation.latitude, data: studyLocation)
            }
        }
        return result
    }

    def getStudyLocationDetails(String studyLocationName) {
        def details = proxyServiceCall(grailsApplication, "getSiteLocationDetails", [siteLocationName: studyLocationName])
        def results = new StudyLocationDetails(details)
        return results
    }

    def getVisitDetails(String visitId) {
        def details = proxyServiceCall(grailsApplication, "getSiteLocationVisitDetails", [siteLocationVisitId: visitId])
        def results = details
        return results
    }

    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.aekosServiceRoot}"
    }

}

