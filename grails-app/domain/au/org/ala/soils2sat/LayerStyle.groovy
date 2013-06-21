package au.org.ala.soils2sat

class LayerStyle {

    String layerName
    String style

    static constraints = {
        layerName nullable: false
        style nullable: true
    }
}
