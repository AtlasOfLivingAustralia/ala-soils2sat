package au.org.ala.soils2sat

class SearchService extends ServiceBase {

    def studyLocationService
    def grailsApplication
    def layerService

    List<StudyLocationVisitSearchResult> searchStudyLocationVisits(UserSearch userSearch) {

        def q = userSearch.searchText?.toLowerCase()

        if (q == "*") {
            q = '' // force match all...
        }
        BoundingBox boundingBox = null
        if (userSearch.useBoundingBox) {
            boundingBox = new BoundingBox(top: userSearch.top, left:  userSearch.left, bottom: userSearch.bottom, right:  userSearch.right)
        }

        def tempResults = []
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
                    // if these study locations match, we add their visits to the temp result sets...
                    def details = studyLocationService.getStudyLocationDetailsOld(studyLocation.siteName)

                    details.data.siteLocationVisitList?.each { visit ->
                        visit.studyLocation = studyLocation
                        tempResults << visit
                    }

                }
            }
        }

        // Now we need to post process the search results and filter out that do not match any specified 'ALA' criteria
        if (userSearch.criteria) {
            def finalResults = []
            tempResults.each { candidate ->
                boolean keep = true
                for (SearchCriteria criteria : userSearch.criteria) {
                    if (!testVisitCriteria(criteria, candidate)) {
                        keep = false
                        break
                    }
                }
                if (keep) {
                    finalResults << candidate
                }
            }
            tempResults = finalResults
        }


        def results = new ArrayList<StudyLocationVisitSearchResult>()

        tempResults.each { visit ->

            def result = new StudyLocationVisitSearchResult(siteName: visit.studyLocation.siteName, date: visit.studyLocation.startDate, longitude: visit.studyLocation.longitude, latitude: visit.studyLocation.latitude, easting: visit.studyLocation.easting, northing: visit.studyLocation.northing, zone: visit.studyLocation.zone as Integer, studyLocationVisitId: visit.id )
            results.add(result)
        }


        return results
    }

    List<StudyLocationSearchResult> searchStudyLocations(UserSearch userSearch) {

        def q = userSearch.searchText?.toLowerCase()

        if (q == "*") {
            q = '' // force match all...
        }
        BoundingBox boundingBox = null
        if (userSearch.useBoundingBox) {
            boundingBox = new BoundingBox(top: userSearch.top, left:  userSearch.left, bottom: userSearch.bottom, right:  userSearch.right)
        }

        def tempResults = []
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
                    tempResults << studyLocation
                }
            }
        }

        // Now we need to post process the search results and filter out that do not match any specified 'ALA' criteria
        if (userSearch.criteria) {
            def finalResults = []
            tempResults.each { candidate ->
                boolean keep = true
                for (SearchCriteria criteria : userSearch.criteria) {
                    if (!testCriteria(criteria, candidate)) {
                        keep = false
                        break
                    }
                }
                if (keep) {
                    finalResults << candidate
                }
            }
            tempResults = finalResults
        }

        def results = new ArrayList<StudyLocationSearchResult>()
        tempResults.each { studyLocation ->
            def result = new StudyLocationSearchResult(siteName: studyLocation.siteName, date: studyLocation.date, longitude: studyLocation.longitude, latitude: studyLocation.latitude, easting: studyLocation.easting, northing: studyLocation.northing, zone: studyLocation.zone as Integer)
            results.add(result)
        }

        return results
    }

    private boolean testVisitCriteria(SearchCriteria criteria, Map candidateVisit) {
        boolean result = false
        def studyLocation = candidateVisit.studyLocation
        switch (criteria.criteriaDefinition.type) {
            case CriteriaType.SpatialPortalLayer:
            case CriteriaType.SpatialPortalField:
                def value = layerService.getIntersectValues(studyLocation.latitude, studyLocation.longitude, [criteria.criteriaDefinition.fieldName])[criteria.criteriaDefinition.fieldName]
                result = SearchCriteriaUtils.eval(criteria, value as String)
                break
            case CriteriaType.StudyLocationVisit:
                result = testVisitCriteriaForVisit(criteria, candidateVisit)
                break
            case CriteriaType.StudyLocation:
                result = testStudyLocationCriteria(criteria, studyLocation)
                break;
            default:
                // Don't care about other types of criteria, let them through!
                result = true
                break
        }
        return result
    }


    private boolean testCriteria(SearchCriteria criteria, Map candidate) {
        boolean result = false
        switch (criteria.criteriaDefinition.type) {
            case CriteriaType.SpatialPortalLayer:
            case CriteriaType.SpatialPortalField:
                def value = layerService.getIntersectValues(candidate.latitude, candidate.longitude, [criteria.criteriaDefinition.fieldName])[criteria.criteriaDefinition.fieldName]
                result = SearchCriteriaUtils.eval(criteria, value as String)
                break
            case CriteriaType.StudyLocationVisit:
                result = testVisitCriteriaForStudyLocation(criteria, candidate)
                break
            case CriteriaType.StudyLocation:
                result = testStudyLocationCriteria(criteria, candidate)
                break;
            default:
                // Don't care about other types of criteria, let them through!
                result = true
                break
        }
        return result
    }

    boolean testStudyLocationCriteria(SearchCriteria criteria, Map candidate) {
        def details = studyLocationService.getStudyLocationDetailsOld(candidate.siteName)
        def valueStr = Eval.x(details.data, 'x.' + criteria.criteriaDefinition.fieldName)
        if (valueStr) {
            return SearchCriteriaUtils.eval(criteria, valueStr as String)
        }
        return false
    }


    boolean testVisitCriteriaForStudyLocation(SearchCriteria criteria, Map candidate) {
        def details = studyLocationService.getStudyLocationDetailsOld(candidate.siteName)
        // at least one of the visits must match the criteria...
        for (def visit : details.data.siteLocationVisitList) {
            if (testVisitCriteriaForVisit(criteria, visit)) {
                return true
            }
        }
        return false
    }

    boolean testVisitCriteriaForVisit(SearchCriteria criteria, Map visit) {
        def valueStr = Eval.x(visit, 'x.' + criteria.criteriaDefinition.fieldName)
        def result = SearchCriteriaUtils.eval(criteria, valueStr as String)
        if (result) {
            return true
        }
        return false
    }

    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.aekosServiceRoot}"
    }

}
