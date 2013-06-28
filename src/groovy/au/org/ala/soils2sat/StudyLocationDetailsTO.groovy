package au.org.ala.soils2sat

class StudyLocationDetailsTO implements Serializable {
    String bioregionName
    String easting
    String firstVisitDate
    String landformElement
    String landformPattern
    String lastVisitDate
    Double latitude
    Double longitude
    String mgaZone
    String northing
    Integer numberOfDistinctPlantSpecies
    Integer numberOfVisits
    List<String> observers
    List<SamplingUnitTO> samplingUnits
    String studyLocationId
    String studyLocationName
}

class SamplingUnitTO implements Serializable {
    String description
    String id
}
