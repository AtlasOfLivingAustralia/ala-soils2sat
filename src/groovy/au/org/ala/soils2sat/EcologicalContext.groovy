package au.org.ala.soils2sat

class EcologicalContext {
    EcologicalContextType1 ecologicalContextType1
    EcologicalContextType2 ecologicalContextType2
    EcologicalContextType3 ecologicalContextType3

    @Override
    public String toString() {
        return "${ecologicalContextType1.description} + ${ecologicalContextType2.description} + ${ecologicalContextType3.description}"
    }
}
