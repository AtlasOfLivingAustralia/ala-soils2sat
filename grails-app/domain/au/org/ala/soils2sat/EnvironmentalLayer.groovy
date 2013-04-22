package au.org.ala.soils2sat

class EnvironmentalLayer implements Serializable {

    String name
    boolean visible
    Float opacity = 1.0

    static constraints = {
        name nullable: false
        visible nullable: true
        opacity nullable: true
    }
}
