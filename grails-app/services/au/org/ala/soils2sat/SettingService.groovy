package au.org.ala.soils2sat

class SettingService {

    public static String OBSERVATION_RADIUS_KEY = "ala.observation.radius"
    public static String OBSERVATION_RANK_KEY = "ala.observation.rank"
    public static String OBSERVATION_FILTER_KEY = "ala.observation.taxonFilter"
    public static String LAYER_STYLES = "ala.layer.styles"

    public float getObservationRadius() {
        def setting = Setting.findByKey(OBSERVATION_RADIUS_KEY)
        if (!setting) {
            setting = new Setting(key: OBSERVATION_RADIUS_KEY, value: "0.5", comment:"The radius of the circle around a location in include when looking for observation records in the ALA (Kms)")
            setting.save()
        }
        return Float.parseFloat(setting.value)
    }

    public String getObservationsRank() {
        def setting = Setting.findByKey(OBSERVATION_RANK_KEY)
        if (!setting) {
            setting = new Setting(key: OBSERVATION_RANK_KEY, value: "species", comment:"The rank of the taxanomic names to return when looking for observation records close to a locality (e.g. species or genus)")
            setting.save()
        }
        return setting.value
    }

    public String getTaxonFilter() {
        def setting = Setting.findByKey(OBSERVATION_FILTER_KEY)
        if (!setting) {
            setting = new Setting(key: OBSERVATION_FILTER_KEY, value: "kingdom:Plantae", comment:"The filter expression to apply to ALA queries (Default 'kingdom:Plantae'")
            setting.save()
        }
        return setting.value
    }

    public String getLayerStyles() {
        def setting = Setting.findByKey(LAYER_STYLES)
        if (!setting) {
            def defaultValues = ['ibra7_regions':'ibra7_labels_style', 'ibra7_subregions':'ibra7_subregion_labels_style']
            setting = new Setting(key: LAYER_STYLES, value: "[]", comment:"The filter expression to apply to ALA queries (Default 'kingdom:Plantae'")
            setting.save()
        }
        return setting.value
    }


}
