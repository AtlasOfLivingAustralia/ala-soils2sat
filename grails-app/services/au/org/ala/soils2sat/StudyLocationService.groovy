package au.org.ala.soils2sat

import org.codehaus.groovy.grails.web.json.JSONElement

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
                    def result = new StudyLocationSearchResult(siteName: studyLocation.siteName, date: studyLocation.date, longitude: studyLocation.longitude, latitude: studyLocation.latitude, easting: studyLocation.easting, northing: studyLocation.northing, zone: studyLocation.zone as Integer)
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

    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.aekosServiceRoot}"
    }

}

public class StudyLocationDetails {

    JSONElement data

    public StudyLocationDetails(JSONElement data) {
        this.data = data
    }

    Integer getId() {
        return data.id as Integer
    }

    String getSiteLocationName() {
        return data.siteLocationName as String
    }

    String getEstablishedDate() {
        return data.establishedDate as String
    }

    String getDescription() {
        return data.description as String
    }

    String getBioregionName() {
        return data.bioregionName as String
    }

    String getProperty() {
        return data.property as String
    }

    String getZone() {
        return data.zone as String
    }

    String getEasting() {
        return data.easting as String
    }

    String getNorthing() {
        return data.northing as String
    }

    String getMethod() {
        return data.method as String
    }

    String getDatum() {
        return data.datum as String
    }

    Boolean getPlotPermanentlyMarkedq() {
        return data.plotPermanentlyMarkedq as Boolean
    }

    Boolean getPlotAlignedToGridq() {
        return data.plotAlignedToGridq as Boolean
    }

    String getLandformPattern() {
        return data.landformPattern as String
    }

    String getLandformElement() {
        return data.landformElement as String
    }

    String getSiteSlope() {
        return data.siteSlope as String
    }

    String getSiteAspect() {
        return data.siteAspect as String
    }

    String getSurfaceStrewSize() {
        return data.surfaceStrewSize as String
    }

    Boolean getPlot100mBy100m() {
        return data.plot100mBy100m as String
    }

    String getComments() {
        return data.comments as String
    }

}
