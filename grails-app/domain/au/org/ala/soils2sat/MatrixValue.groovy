package au.org.ala.soils2sat

class MatrixValue implements Serializable {

    EcologicalContext ecologicalContext
    Question question
    Boolean required

    static constraints = {
        required nullable: true
    }

}
