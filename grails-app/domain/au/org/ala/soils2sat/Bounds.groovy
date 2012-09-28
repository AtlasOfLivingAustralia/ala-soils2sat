package au.org.ala.soils2sat

class Bounds {

    double left = 0
    double top = 0
    double right = 0
    double bottom = 0


    static constraints = {
        left nullable: false
        top nullable: false
        right nullable: false
        bottom nullable: false
    }

    static mapping = {
   		left column: '`left`'
        right column: '`right`'
        top column:  '`top`'
        bottom column: '`bottom`'
   	}

}
