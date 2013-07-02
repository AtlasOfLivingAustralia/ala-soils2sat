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

<div>
    <h4>${criteria?.criteriaDefinition?.name}</h4>
    <div class="alert alert-error" id="errorMessageDiv" style="display: none">
    </div>
    <form id="criteriaForm">
        <g:hiddenField name="criteriaId" value="${criteria?.id}" />
        <g:hiddenField name="searchCriteriaDefinitionId" value="${criteria?.criteriaDefinition?.id}" />
        <div id="criteriaDetail"></div>
    </form>
</div>

<div id="buttonDiv">
    <button id="btnSaveCriteria" type="button" class="btn btn-small btn-primary pull-right">Save changes</button>
    <button id="btnCancelEdit" style="margin-right: 10px" type="button" class="btn btn-small pull-right">Cancel</button>
</div>


<script type="text/javascript">

    $("#criteriaDetail").html("<sts:spinner/>");

    $.ajax("${createLink(action: "ajaxCriteriaDetailFragment", params:[criteriaId:criteria?.id, searchCriteriaDefinitionId: criteria?.criteriaDefinition?.id])}").done(function(content) {
        $("#criteriaDetail").html(content);
    });

    $("#btnCancelEdit").click(function(e) {
        e.preventDefault();
        hideModal();
    });

    $("#btnSaveCriteria").click(function(e) {
        var formData = $("#criteriaForm").serialize();
        var errorDiv = $("#errorMessageDiv");
        errorDiv.css("display",'none');
        $.post('${createLink(action:'addSearchCriteriaAjax')}',formData, function(data) {
            if (data.errorMessage) {
                errorDiv.html(data.errorMessage);
                errorDiv.css("display",'block');
            } else {
                hideModal();
            }

        });
    });



</script>