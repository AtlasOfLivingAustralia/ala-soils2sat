package au.org.ala.soils2sat

import javax.persistence.Transient

class UserApplicationState implements Serializable {

    // List<StudyLocation> selectedPlots
    List<StudyLocationVisit> selectedVisits
    List<EnvironmentalLayer> layers
    Boolean plotOnlySelectedLocations
    Bounds viewExtent
    Date lastLogin
    UserSearch currentSearch
    String sidebarSelectedTab

    static hasMany = [layers: EnvironmentalLayer, selectedVisits:StudyLocationVisit]

    static belongsTo = [user:User]

    static constraints = {
        // selectedPlots nullable: true
        selectedVisits nullable: true
        layers nullable: true
        plotOnlySelectedLocations nullable:true
        viewExtent nullable: true
        lastLogin nullable: true
        currentSearch nullable: true
        sidebarSelectedTab nullable: true
    }

    @Transient
    public boolean containsLayerName(String layerName) {
        def existing = layers?.find {
            it.name == layerName
        }
        return existing != null
    }

    @Transient
    public List<String> getSelectedPlotNames() {
        return selectedVisits.collect { it.studyLocationName }
    }

    @Transient
    public boolean containsPlot(String name) {
        def existing = getSelectedPlotNames()?.find {
            it == name
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
