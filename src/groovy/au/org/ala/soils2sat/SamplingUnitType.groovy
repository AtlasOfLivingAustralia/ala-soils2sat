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

public enum SamplingUnitType {

    PointIntercept(0),
    BasalArea(1),
    StructuralSummary(4),
    SoilObservation(5),
    SoilBulkDensity(6),
    SoilCharacter(7)

    private final int value

    SamplingUnitType(int value) {
        this.value = value
    }

    public int getValue() {
        return value
    }

    public String getValueAsString() {
        return value.toString()
    }

    public static SamplingUnitType parse(String str) {
        SamplingUnitType candidate = null

        if (str.isNumber()) {
            int val = Integer.parseInt(str)
            candidate = values().find { it.value == val }
        } else {
            candidate = values().find { it.toString().equalsIgnoreCase(str) }
        }

        return candidate
    }


}
