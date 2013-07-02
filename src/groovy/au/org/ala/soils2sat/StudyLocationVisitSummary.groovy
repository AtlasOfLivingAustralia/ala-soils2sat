/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

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
