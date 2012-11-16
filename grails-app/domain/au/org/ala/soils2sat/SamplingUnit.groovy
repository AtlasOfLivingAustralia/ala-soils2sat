package au.org.ala.soils2sat

class SamplingUnit {

    String name

    static constraints = {
        name nullable: false, blank: false
    }
}
