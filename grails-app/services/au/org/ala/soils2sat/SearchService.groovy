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

import org.apache.cxf.jaxrs.ext.search.client.CompleteCondition
import org.apache.cxf.jaxrs.ext.search.client.SearchConditionBuilder

class SearchService extends ServiceBase {

    def studyLocationService
    def grailsApplication
    def layerService

    List<StudyLocationVisitSearchResult> searchStudyLocationVisits(UserSearch userSearch) {

        def q = userSearch.searchText?.toUpperCase()

        if (q == "*") {
            q = '' // force match all...
        }
        BoundingBox boundingBox = null
        if (userSearch.useBoundingBox) {
            boundingBox = new BoundingBox(top: userSearch.top, left:  userSearch.left, bottom: userSearch.bottom, right:  userSearch.right)
        }

        def b = SearchConditionBuilder.instance()
        CompleteCondition condition

        if (q) {
            condition = b.is("studyLocationName").equalTo("*${q}*")
        } else {
            condition = b.is("studyLocationName").equalTo("*")
        }

        if (boundingBox) {
            condition = condition.and().and(
                b.is("latitude").lessOrEqualTo(Math.max(boundingBox.top, boundingBox.bottom)),
                b.is("latitude").greaterOrEqualTo(Math.min(boundingBox.top, boundingBox.bottom)),
                b.is("longitude").lessOrEqualTo(Math.max(boundingBox.left, boundingBox.right)),
                b.is("longitude").greaterOrEqualTo(Math.min(boundingBox.left, boundingBox.right))
            )
        }

        // now we have to loop through our criteria and add any AusPlots criteria to the FIQL.
        def ausplotsCriteria = userSearch.criteria.findAll { it.criteriaDefinition.type == CriteriaType.AusplotsSearchTrait }
        ausplotsCriteria?.each { c ->
            def e = SearchCriteriaUtils.factory(c)
            if (e) {
                condition = e.createFIQLCondition(c, condition.and()) ?: condition
            }
        }

        def fiql = condition.query()


            def tempResults = studyLocationService.fiqlSearch(fiql)
//        // TODO, ultimately the search results will contain lat/long, so no need to do this step...
//        tempResults?.each { result ->
//            def location = studyLocationService.getStudyLocationDetails(result.studyLocationName)
//            if (location) {
//                result.latitude = location.latitude
//                result.longitude = location.longitude
//            }
//        }


        // Now we need to post process the search results and filter out that do not match any specified 'ALA' criteria
        if (userSearch.criteria) {
            def finalResults = []
            tempResults.each { candidate ->
                boolean keep = true
                for (SearchCriteria criteria : userSearch.criteria) {
                    if (!testVisitCriteria(criteria, candidate)) {
                        keep = false
                        break
                    }
                }
                if (keep) {
                    finalResults << candidate
                }
            }
            tempResults = finalResults
        }

        return tempResults
    }

    private boolean testVisitCriteria(SearchCriteria criteria, StudyLocationVisitSearchResult candidate) {
        boolean result
        switch (criteria.criteriaDefinition.type) {
            case CriteriaType.SpatialPortalLayer:
            case CriteriaType.SpatialPortalField:
                def value = layerService.getIntersectValues(candidate.latitude, candidate.longitude, [criteria.criteriaDefinition.fieldName])[criteria.criteriaDefinition.fieldName]
                result = SearchCriteriaUtils.eval(criteria, value as String)
                break
            default:
                // Don't care about other types of criteria, let them through!
                result = true
                break
        }
        return result
    }

    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.aekosServiceRoot}"
    }

}
