package au.org.ala.soils2sat

class SearchCriteria {

    SearchCriteriaDefinition criteriaDefinition
    String value

    static constraints = {
        value nullable: true
    }

}
