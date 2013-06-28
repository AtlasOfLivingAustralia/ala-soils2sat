<%@ page import="au.org.ala.soils2sat.AttachmentCategory; au.org.ala.soils2sat.AttachmentOwnerType" %>
<g:if test="${!attachment?.id}">
    <div class="control-group">
        <label class="control-label" for='file'>File:</label>

        <div class="controls">
            <input type="file" name="file" id="file" multiple="">
        </div>
    </div>
</g:if>
<g:else>

    <div class="control-group">
        <label class="control-label" for='file'>Name:</label>
        <div class="controls">
            <g:textField class="input-xxlarge" name="name" value="${attachment?.name}" />
        </div>
    </div>

</g:else>

<div class="control-group">
    <label class="control-label" for='attachedTo'>Attach to:</label>
    <div class="controls">
        <g:radioGroup values="${AttachmentOwnerType.values()}" labels="${AttachmentOwnerType.values()*.name()}" name="attachedTo" value="${attachment?.attachedTo}">
            <span>
                ${it.label}&nbsp;&nbsp;${it.radio}&nbsp;&nbsp;&nbsp;
            </span>
        </g:radioGroup>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for='studyLocationName'>Study Location Name:</label>

    <div class="controls">
        <g:textField name="studyLocationName" id="studyLocationName" value="${attachment?.studyLocationName}" />
    </div>
</div>

<div id="visitDiv" class="control-group">
    <label class="control-label" for='studyLocationVisitStartDate'>Visit Start Date:</label>
    <div class="controls">
        <g:textField name="studyLocationVisitStartDate" id="studyLocationVisitStartDate" value="${attachment?.studyLocationVisitStartDate}" />
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="category">Category:</label>
    <div class="controls">
        <g:select name="category" id="category" from="${AttachmentCategory.values()}" value="${attachment?.category}" />
    </div>
</div>



<div class="control-group">
    <label class="control-label" for='comment'>Comment:</label>

    <div class="controls">
        <g:textArea class="input-xxlarge" name="comment" id="comment" value="${attachment?.comment}"  rows="4" cols="80" />
    </div>
</div>

<script type='text/javascript'>

    $(document).ready(function () {

        $("[name='attachedTo']").change(function(e) {
           showHideAttachedTo();
        });

        $("#studyLocationName").autocomplete({
            source: "${createLink(controller: 'studyLocation', action:'ajaxStudyLocationAutocomplete')}"
        });

        $("#studyLocationVisitStartDate").autocomplete({
            source: function(request, callback) {
                var studyLocationName = $("#studyLocationName").val();
                if (studyLocationName) {
                    $.ajax("${createLink(controller:'studyLocation', action:'ajaxVisitStartDateAutocomplete')}?studyLocationName=" + studyLocationName + "&term=" + request.term).done(function(data) {
                        callback(data);
                    });
                } else {
                    callback();
                }

            }
        });

        <g:if test="${attachment?.attachedTo}">
        $('[name="attachedTo"][value="${attachment?.attachedTo?.toString()}"]').prop("checked", true);
        </g:if>

        showHideAttachedTo();

    });

    function showHideAttachedTo() {
        var selected = $("[name='attachedTo']:checked");
        if (selected) {
            var value = $(selected).val();
            if (value == 'StudyLocation') {
                $("#visitDiv").css("display", "none");
            } else {
                $("#visitDiv").css("display", "block");
            }
        }
    }

</script>

