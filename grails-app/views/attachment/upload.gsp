<%@ page import="au.org.ala.soils2sat.AttachmentCategory" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Attachment</title>
    </head>

    <body>
        <script type='text/javascript'>

            $(document).ready(function () {

                $("[name='attachTo']").change(function(e) {
                   showHideAttachTo();
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

                <g:if test="${params.attachTo}">
                $('[name="attachTo"][value="${params.attachTo}"]').prop("checked", true);
                </g:if>

                showHideAttachTo();

            });

            function showHideAttachTo() {
                var selected = $("[name='attachTo']:checked");
                if (selected) {
                    var value = $(selected).val();
                    if (value == 'studyLocation') {
                        $("#visitDiv").css("display", "none");
                    } else {
                        $("#visitDiv").css("display", "block");
                    }
                }
            }

        </script>

        <content tag="pageTitle">Attachments</content>

        <div id='login' class="container-fluid">

            <h3>Upload Attachment</h3>

            <g:form class="form-horizontal" controller="attachment" action="saveAttachment" enctype="multipart/form-data">

                <div class="control-group">
                    <label class="control-label" for='file'>File:</label>

                    <div class="controls">
                        <input type="file" name="file" id="file" multiple="">
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='attachTo'>Attach to:</label>
                    <div class="controls">
                        <g:radioGroup values="['studyLocation', 'studyLocationVisit']" labels="['Study Location', 'Study Location Visit']" name="attachTo" value="studyLocation">
                            <span style="vertical-align: middle">${it.label}</span> <span style="vertical-align: middle">${it.radio}</span>
                        </g:radioGroup>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='studyLocationName'>Study Location Name:</label>

                    <div class="controls">
                        <g:textField name="studyLocationName" id="studyLocationName" value="${params.studyLocationName}" />
                    </div>
                </div>

                <div id="visitDiv" class="control-group">
                    <label class="control-label" for='studyLocationVisitStartDate'>Visit Start Date:</label>
                    <div class="controls">
                        <g:textField name="studyLocationVisitStartDate" id="studyLocationVisitStartDate" value="${params.studyLocationVisitStartDate}" />
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="category">Category:</label>
                    <div class="controls">
                        <g:select name="category" id="category" from="${AttachmentCategory.values()}" value="${params.category}" />
                    </div>
                </div>



                <div class="control-group">
                    <label class="control-label" for='comment'>Comment:</label>

                    <div class="controls">
                        <g:textArea class="input-xxlarge" name="comment" id="comment" value="${params.comment}"  rows="4" cols="80" />
                    </div>
                </div>

                <div class="control-group">
                    <div class="controls">
                        <button type="submit" class="btn btn-small btn-primary"><i class="icon-upload icon-white"></i>&nbsp;Upload</button>
                    </div>
                </div>

            </g:form>
        </div>
    </body>
</html>