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

    @Cacheable(value="S2S_StudyLocationCache", key="{#root.methodName,#studyLocationName}")
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

    @CacheFlush("S2S_LayerCache")
    public void flushCache() {
        logService.log("Flushing Layer Cache")
    }


    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.aekosServiceRoot}"
    }

}

