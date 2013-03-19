package au.org.ala.soils2sat

import javax.persistence.Transient

class UserApplicationState {

    List<StudyLocation> selectedPlots
    List<StudyLocationVisit> selectedVisits
    List<EnvironmentalLayer> layers
    Boolean plotOnlySelectedLocations
    Bounds viewExtent
    Date lastLogin
    UserSearch currentSearch
    MapSelectionMode mapSelectionMode = MapSelectionMode.StudyLocation

    static hasMany = [selectedPlots:StudyLocation, layers: EnvironmentalLayer, selectedVisits:StudyLocationVisit]

    static belongsTo = [user:User]

    static constraints = {
        selectedPlots nullable: true
        selectedVisits nullable: true
        layers nullable: true
        plotOnlySelectedLocations nullable:true
        viewExtent nullable: true
        lastLogin nullable: true
        currentSearch nullable: true
        mapSelectionMode nullable: true
    }

    @Transient
    public boolean containsLayerName(String layerName) {
        def existing = layers?.find {
            it.name == layerName
        }
        return existing != null
    }

    @Transient
    public boolean containsPlot(String name) {
        def existing = selectedPlots?.find {
            it.name == name
        }

        return existing != null
    }

    @Transient
    public boolean containsVisit(String visitId) {
        def existing = selectedVisits?.find {
            it.studyLocationVisitId == visitId
        }
    }

}
