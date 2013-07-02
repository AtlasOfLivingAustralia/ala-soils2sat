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

<div class="form-horizontal">

    <div class="alert alert-error" id="errorMessageDiv" style="display: none">
    </div>

    <form id="criteriaForm">
        <div class="control-group">
            <label class="control-label" for='searchCriteriaDefinitionId'>Criteria:</label>
            <div class="controls">
                <g:select id="cmbCriteria" name="searchCriteriaDefinitionId" from="${criteriaDefinitions}" optionValue="name" optionKey="id" noSelection="${[0:"<Select Criteria>"]}" />
            </div>
        </div>
        <div id="criteriaDetail"></div>
    </form>

    <div id="addButtonDiv" style="display: none">
        <button id="btnSaveCriteria" type="button" class="btn btn-small btn-primary pull-right">Add criteria</button>
    </div>

</div>

<script type="text/javascript">

    $("#cmbCriteria").change(function(e) {
        $("#criteriaDetail").html("<sts:spinner/>");
        var criteriaDefinitionId = $(this).val();
        if (criteriaDefinitionId == 0) {
            $("#criteriaDetail").html("");
            $("#addButtonDiv").css('display', 'none');
        } else {
            $.ajax("${createLink(action: "ajaxCriteriaDetailFragment")}?searchCriteriaDefinitionId=" + criteriaDefinitionId).done(function(content) {
                $("#addButtonDiv").css("display", "block");
                $("#criteriaDetail").html(content);
            });
        }
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