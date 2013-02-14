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