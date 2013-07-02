%{--
  - ﻿Copyright (C) 2013 Atlas of Living Australia
  - All Rights Reserved.
  -
  - The contents of this file are subject to the Mozilla Public
  - License Version 1.1 (the "License"); you may not use this file
  - except in compliance with the License. You may obtain a copy of
  - the License at http://www.mozilla.org/MPL/
  -
  - Software distributed under the License is distributed on an "AS
  - IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
  - implied. See the License for the specific language governing
  - rights and limitations under the License.
  --}%

<%@ page import="au.org.ala.soils2sat.CriteriaValueType" %>
<div>
    <g:if test="${userSearch?.criteria}">
        <h5>Each of the following criteria must be met:</h5>
        <g:each in="${userSearch.criteria}" var="criteria">
            <div class="alert alert-info" style="color: black; padding-right: 10px">
                <table style="width: 100%">
                    <tr searchCriteriaId="${criteria.id}">
                        <td>
                            <g:if test="${criteria.criteriaDefinition.valueType == CriteriaValueType.Boolean}">
                                <strong>${criteria.criteriaDefinition.description}</strong>&nbsp;
                            </g:if>
                            <g:else>
                                <strong>${criteria.criteriaDefinition.name}</strong>&nbsp;
                            </g:else>
                            ${au.org.ala.soils2sat.SearchCriteriaUtils.format(criteria, { str -> "<strong>${str}</strong>" as String} )}
                        </td>
                        <td style="width: 80px">
                            <button type="button" class="btn btn-mini pull-right btnDeleteCriteria" title="Remove this search criteria"><i class="icon-remove"></i></button>&nbsp;
                            <button type="button" style="margin-right:5px" class="btn btn-mini pull-right btnEditCriteria" title="Edit search criteria"><i class="icon-edit"></i></button>
                        </td>
                    </tr>
                </table>

            </div>
        </g:each>
    </g:if>
</div>

<script type="text/javascript">

    $(".btnDeleteCriteria").click(function(e) {
        e.preventDefault();
        var criteriaId = $(this).parents("tr[searchCriteriaId]").attr("searchCriteriaId");
        if (criteriaId) {
            $.ajax("${createLink(action:'deleteSearchCriteria', params:[userSearchId: userSearch?.id])}&searchCriteriaId=" + criteriaId).done(function(results) {
                renderCriteria();
            });
        }
    });

    $(".btnEditCriteria").click(function(e) {
        e.preventDefault();
        var criteriaId = $(this).parents("tr[searchCriteriaId]").attr("searchCriteriaId");
        if (criteriaId) {
            showModal({
                url: "${createLink(action:'ajaxEditSearchCriteriaFragment')}?criteriaId=" + criteriaId,
                title: "Edit search criteria",
                height: 520,
                width: 700,
                onClose: function () {
                    renderCriteria();
                }
            });
        }
    });


</script>