package au.org.ala.soils2sat

class SamplingUnit implements Serializable {

    String name

    static constraints = {
        name nullable: false, blank: false
    }
}
