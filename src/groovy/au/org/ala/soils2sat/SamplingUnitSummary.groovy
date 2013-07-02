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
