package au.org.ala.soils2sat

import org.codehaus.groovy.grails.web.json.JSONElement

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
