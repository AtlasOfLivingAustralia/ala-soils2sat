package au.org.ala.soils2sat

class UserApplicationState {

    List<StudyLocation> selectedPlots
    List<EnvironmentalLayer> layers
    Boolean plotOnlySelectedLocations
    Bounds viewExtent

    static hasMany = [selectedPlots:StudyLocation, layers: EnvironmentalLayer]

    static belongsTo = [user:User]

    static constraints = {
        selectedPlots nullable: true
        layers nullable: true
        plotOnlySelectedLocations nullable:true
        viewExtent nullable: true
    }

    public boolean containsLayerName(String layerName) {
        def existing = layers?.find {
            it.name == layerName
        }
        return existing != null
    }

    public boolean containsPlot(String name) {
        def existing = selectedPlots?.find {
            it.name == name
        }

        return existing != null
    }
}
