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

<%@ page import="au.org.ala.soils2sat.SearchCriteriaUtils" %>
<div>
    <div>

        <g:if test="${value}">
            <g:set var="evaluator" value="${new SearchCriteriaUtils.DateRangeCriteriaTranslator(value as String)}" />
        </g:if>

        <label class="radio inline" style="white-space: nowrap">
            <g:radio class="radioButton" name="operator" value="lt" checked="${evaluator == null || evaluator?.operator == 'lt' ? 'checked' : ''}"/>
            Before
        </label>
        <label class="radio inline" style="white-space: nowrap">
            <g:radio class="radioButton" name="operator" value="gt" checked="${evaluator?.operator == 'gt' ? 'checked' : ''}" />
            After
        </label>
        <label class="radio inline" style="white-space: nowrap">
            <g:radio class="radioButton" name="operator" value="bt" checked="${evaluator?.operator == 'bt' ? 'checked' : ''}" />
            Between
        </label>
    </div>
    <div style="margin-top: 15px">
        <g:textField class="dateValue" name="dateValue1" id="dateValue1" value="${evaluator?.startDate?.format("dd/MM/yyyy")}" />

        <span class="dateRangeOther" style="display: ${evaluator?.operator == 'bt' ? 'inline-block' : 'none'}">
            &nbsp;and&nbsp;
            <g:textField class="dateValue" name="dateValue2" id="dateValue2" value="${evaluator?.endDate?.format("dd/MM/yyyy")}" />
        </span>
    </div>
</div>
<script type="text/javascript">

    $(document).ready(function() {

        $(".radioButton").click(function(e) {
            var operator = $(this).val();
            if (operator == "bt") {
                $(".dateRangeOther").css("display", "inline-block");
            } else {
                $(".dateRangeOther").css("display", "none");
            }
        });

        $(".dateValue").datepicker({
            dateFormat: 'dd/mm/yy'
        });

    });

</script>
