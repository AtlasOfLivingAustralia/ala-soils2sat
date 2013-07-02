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

class LastDailyId implements Serializable {

    private final static byte[] _lock = []

    Date date
    int lastNumber

    static constraints = {
        date nullable: false
        lastNumber nullable: false
    }

    private static LastDailyId instance() {
        synchronized (_lock) {
            def instanceList = LastDailyId.list()
            if (!instanceList) {
                def newInstance = new LastDailyId(date: new Date(), lastNumber: 0)
                newInstance.save(flush: true, failOnError: true)
                // No instance exists, so create one
                return newInstance
            }
            return instanceList[0]
        }

    }

    static LastDailyId getNextNumber() {
        synchronized (_lock) {
            Date today = new Date()
            LastDailyId lastNumber = instance()
            if (!DateUtils.isSameDay(lastNumber.date, today)) {
                lastNumber.date = today
                lastNumber.lastNumber = 0
            }
            lastNumber.lastNumber++
            lastNumber.save(flush:true, failOnError: true)
            lastNumber
        }
    }

}

