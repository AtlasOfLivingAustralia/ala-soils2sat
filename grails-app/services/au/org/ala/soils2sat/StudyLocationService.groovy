package au.org.ala.soils2sat

import grails.plugin.springcache.annotations.CacheFlush
import org.codehaus.groovy.grails.web.json.JSONElement
import org.springframework.cache.annotation.Cacheable

class StudyLocationService extends ServiceBase {

    def grailsApplication
    def layerService
    def logService

    @Cacheable("S2S_StudyLocationCache")
    List<StudyLocationSearchResult> getStudyLocations() {
        def studyLocations = proxyServiceCall(grailsApplication, "getStudyLocations")
        def results = new ArrayList<StudyLocationSearchResult>()
        studyLocations?.results?.each { studyLocation ->
            def result = new StudyLocationSearchResult(siteName: studyLocation.siteName, date: studyLocation.date, longitude: studyLocation.longitude, latitude: studyLocation.latitude)
            results.add(result)
        }

        return results
    }

    // @Cacheable(value="S2S_StudyLocationCache", key="{#root.methodName,#studyLocationName}")
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

    @Cacheable(value="S2S_StudyLocationCache", key="{#root.methodName,#studyLocationName}")
    def getStudyLocationDetails(String studyLocationName) {
        def details = proxyServiceCall(grailsApplication, "getSiteLocationDetails", [siteLocationName: studyLocationName])
        def results = new StudyLocationDetails(details)
        return results
    }

    @Cacheable(value="S2S_StudyLocationCache", key="{#root.methodName,#visitId}")
    def getVisitDetails(String visitId) {
        def details = proxyServiceCall(grailsApplication, "getSiteLocationVisitDetails", [siteLocationVisitId: visitId])
        def results = details
        return results
    }

    @Cacheable(value="S2S_StudyLocationCache", key="{#root.methodName}")
    def getSearchTraits() {
        def traits = proxyServiceCall(grailsApplication, "getSearchTraits", [])
        return traits
    }

    @CacheFlush("S2S_LayerCache")
    public void flushCache() {
        logService.log("Flushing Layer Cache")
    }


    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.aekosServiceRoot}"
    }

    @Cacheable(value="S2S_StudyLocationCache", key="{#root.methodName,#siteId}")
    public getStudyLocationNameForId(String siteId) {
        def locations = getStudyLocations()
        def names = []
        locations.each { location ->
            names << location.siteName
        }

        def results = proxyServiceCall(grailsApplication, "getStudyLocationSummary", [siteNames: names.join(",")])?.studyLocationSummaryList
        for (def location : results) {
            if (location.siteLocSysId?.toString() == siteId) {
                return location.siteLocationName
            }
        }

        return null
    }

    @Cacheable(value="S2S_StudyLocationCache", key="{#root.methodName,#visitId}")
    public String getStudyLocatioNameForVisitId(String visitId) {
        def visitDetails = getVisitDetails(visitId)
        return getStudyLocationNameForId(visitDetails?.siteLocationId?.toString())
    }

}

