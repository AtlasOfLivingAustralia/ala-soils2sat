package au.org.ala.soils2sat

/**
 * Created with IntelliJ IDEA.
 * User: baird
 * Date: 16/01/13
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
class SamplingUnitSummary {

    public StudyLocationVisitSummary visit
    public Map data

    public Date getSampleDate() {
        return DateUtils.tryParse(data.sampleDate)
    }

    public List<String> getObserverNames() {

    }

    public String getDescription() {
        return data.samplingUnitDescription
    }

    public String getSamplingUnit() {
        return data.samplingUnit
    }

    public String getSummary() {
        return data.summary
    }

}
