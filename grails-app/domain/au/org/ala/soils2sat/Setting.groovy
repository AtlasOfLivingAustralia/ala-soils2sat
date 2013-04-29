package au.org.ala.soils2sat

class Setting {

    String key
    String value
    String comment

    static constraints = {
        key nullable: false
        value nullable: true
        comment nullable: true
    }

}
