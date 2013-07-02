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

class DataExtraction implements Serializable {

    String packageName
    Date date
    String username
    String localFile
    Integer downloadCount = 0
    String doi
    List studyLocationVisits
    Date firstVisitDate
    Date lastVisitDate
    String appVersion

    static hasMany = [studyLocationVisits: String]

    static constraints = {
        downloadCount nullable: true
        doi nullable: true
        firstVisitDate nullable: true
        lastVisitDate nullable: true
        appVersion nullable: true
    }

    def afterDelete() {
        // Clean up the local file.
        try {
            def f = new File(localFile)
            if (f.exists()) {
                f.delete()
            }
        } catch (Exception ex) {
            println "Error occurred during 'afterDelete' event on DataExtraction (PackageName $packageName)"
            ex.printStackTrace()
        }
    }
}
