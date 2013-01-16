package au.org.ala.soils2sat

import org.apache.commons.lang.WordUtils

/**
 * Created with IntelliJ IDEA.
 * User: baird
 * Date: 24/10/12
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
class StudyLocationSummary {
    String name
    double latitude
    double longitude
    Map data

    /**
     * @return The date the first visit to this study location was made. If not present in the top level data hierarchy,
     * the visit list will be iterated over, and the first date will ne calculated
     */
    public Date getFirstVisitDate() {
        def date = DateUtils.tryParse(data.firstVisitDate)
        if (!date) {
            def visitList = data.visitSummaryList
            if (visitList) {
                visitList.each { visit ->
                    def candidate = DateUtils.tryParse(visit.startDate)
                    if (candidate) {
                        if (date == null) {
                            date = candidate
                        } else {
                            if (candidate < date) {
                                date = candidate
                            }
                        }
                    }
                }
            }
        }
        return date
    }

    /**
     *
     * @return The last date on which a visit occurred. Will either be present in the top level of the data hierarchy, or
     * will be calculated by iterating over each visit. As a last resort the first visit date will be used.
     */
    public Date getLastVisitDate() {
        def date = DateUtils.tryParse(data.lastVisitDate)
        if (!date) {
            def visitList = data.visitSummaryList
            if (visitList) {
                visitList.each { visit ->
                    def candidate = DateUtils.tryParse(visit.endDate)
                    if (candidate) {
                        if (date == null) {
                            date = candidate
                        } else {
                            if (candidate > date) {
                                date = candidate
                            }
                        }
                    }
                }
            }
        }
        if (!date) {
            date = getFirstVisitDate()
        }

        return date
    }

    /**
     *
     * @return A distinct list of all the observers who have visited this study location
     */
    public List<String> getObservers() {
        def observers = new ArrayList<String>()
        def visitList = data.visitSummaryList
        if (visitList) {
            visitList.each { visit ->
                def observerList = visit.observerList
                if (observerList) {
                    observerList.each {
                        def observer = it.name as String
                        def formatted = WordUtils.capitalizeFully(observer.replaceAll('_', ' '))
                        if (!observers.contains(formatted)) {
                             observers.add(formatted)
                        }
                    }
                }
            }
        }

        return observers
    }

    public List<StudyLocationVisitSummary> getVisitSummaries() {
        def summaries = new ArrayList<StudyLocationVisitSummary>()
        def visitList = data.visitSummaryList
        if (visitList) {
            visitList.each { visit ->
                def visitSummary = new StudyLocationVisitSummary(data: visit, studyLocation: this)
                summaries.add(visitSummary)
            }
        }
        return summaries
    }

}
