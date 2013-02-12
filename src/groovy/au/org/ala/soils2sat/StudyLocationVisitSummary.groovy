package au.org.ala.soils2sat

import org.apache.commons.lang.WordUtils

/**
 * Created with IntelliJ IDEA.
 * User: baird
 * Date: 16/01/13
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
class StudyLocationVisitSummary {

    public StudyLocationSummary studyLocation
    public Map data

    String getVisitId() {
        return data.sysId as String
    }

    public Date getStartDate() {
        Date date = DateUtils.tryParse(data.startDate)
        return date
    }

    public Date getEndDate() {
        Date date = DateUtils.tryParse(data.endDate)
        return date ?: getStartDate()
    }

    public List<String> getObservers() {
        def names = new ArrayList<String>()
        def observerList = data.observerList
        if (observerList) {
            observerList.each {
                def observer = it.name as String
                def formatted = WordUtils.capitalizeFully(observer.replaceAll('_', ' '))
                if (!names.contains(formatted)) {
                     names.add(formatted)
                }
            }
        }

        return names
    }

    public List<String> getSamplingUnitNames() {
        def names = new ArrayList<String>()
        def samplingUnits = data.samplingUnitNameList
        if (samplingUnits) {
            samplingUnits.each { samplingUnit ->
                names.add(samplingUnit)
            }
        }
        return names
    }

    public List<SamplingUnitSummary> getSamplingUnitSummaryList() {
        def list = new ArrayList<SamplingUnitSummary>()
        data?.samplingUnitSummaryList?.each { su ->
            def unitSummary = new SamplingUnitSummary(data: su, visit: this)
            list.add(unitSummary)
        }

        return list
    }
}
