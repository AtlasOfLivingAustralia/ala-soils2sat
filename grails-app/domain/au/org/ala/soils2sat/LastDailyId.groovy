package au.org.ala.soils2sat

class LastDailyId {

    Date date
    int lastNumber

    static constraints = {
        date nullable: false
        lastNumber nullable: false
    }

}
