package au.org.ala.soils2sat

class SearchCriteriaDefinition implements Serializable {

    CriteriaType type
    CriteriaValueType valueType
    String name
    String description
    String fieldName

    static constraints = {
        valueType nullable: false
        name nullable: false
        description nullable: true
    }
}
