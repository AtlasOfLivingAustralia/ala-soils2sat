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