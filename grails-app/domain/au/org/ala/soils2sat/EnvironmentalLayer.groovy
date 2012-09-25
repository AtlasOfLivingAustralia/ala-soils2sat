package au.org.ala.soils2sat

class EnvironmentalLayer {

    String name
    boolean visible

    static constraints = {
        name nullable: false
        visible nullable: true
    }
}
