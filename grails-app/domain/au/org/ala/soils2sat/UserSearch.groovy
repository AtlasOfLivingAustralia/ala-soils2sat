package au.org.ala.soils2sat

class UserSearch {

    User user
    String name
    String searchText
    Boolean useBoundingBox
    Double top
    Double left
    Double bottom
    Double right
    List<SearchCriteria> criteria

    static belongsTo = [user: User]
    static hasMany = [criteria: SearchCriteria]

    static mapping = {
        top column: "bbox_top"
        left column: "bbox_left"
        right column: "bbox_right"
        bottom column: "bbox_bottom"
    }

    static constraints = {
        user nullable: false
        name nullable:  false
        searchText nullable: true
        top nullable: true
        left nullable: true
        bottom nullable: true
        right nullable: true
        useBoundingBox nullable: true
    }

}
