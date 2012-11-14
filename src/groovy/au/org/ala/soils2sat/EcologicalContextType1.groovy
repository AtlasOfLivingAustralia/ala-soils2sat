package au.org.ala.soils2sat

enum EcologicalContextType1 {
    FAUNA("Fauna"),
    MICROORGANISM("Micro-organism"),
    SOIL("Soil"),
    VEGETATION("Vegetation/Flora")

    public EcologicalContextType1(String description) {
        _description = description
    }

    String getDescription() {
        return _description;
    }

    private String _description
}

enum EcologicalContextType2 {

    STRUCTURE("Structure"),
    COMPOSITION("Composition"),
    FUNCTION("Function")

    public EcologicalContextType2(String description) {
        _description = description
    }

    String getDescription() {
        return _description;
    }

    private String _description

}

enum EcologicalContextType3 {

    ECOSYSTEM("Ecosystem"),
    GENE("Gene"),
    INDIVIDUAL("Individual"),
    POPULATION("Population"),
    BIOME("Biome"),
    COMMUNITY("Community")

    public EcologicalContextType3(String description) {
        _description = description
    }

    String getDescription() {
        return _description;
    }

    private String _description

}


