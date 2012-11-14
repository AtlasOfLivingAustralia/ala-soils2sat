package au.org.ala.soils2sat

class MatrixValue {

    EcologicalContextType1 ecologicalContextAxis1
    EcologicalContextType2 ecologicalContextAxis2
    EcologicalContextType3 ecologicalContextAxis3
    Question question
    Boolean required

    static constraints = {
        required nullable: true
    }

    transient String getEcologicalContext() {
        return "${ecologicalContextAxis1.description} + ${ecologicalContextAxis2.description} + ${ecologicalContextAxis3.description}"
    }

}
