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

            <g:form class="form-horizontal" controller="attachment" action="saveAttachment">

                <div class="control-group">
                    <label class="control-label" for='file'>File:</label>

                    <div class="controls">
                        <input type="file" name="file" id="file">
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
                    <label class="control-label" for='studyLocation'>Study Location Name:</label>

                    <div class="controls">
                        <g:textField name="studyLocation" id="studyLocationName" />
                    </div>
                </div>

                <div id="visitDiv" class="control-group">
                    <label class="control-label" for='studyLocationVisit'>Visit Start Date:</label>
                    <div class="controls">
                        <g:textField name="studyLocationVisit" id="studyLocationVisitStartDate" />
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='comment'>Comment:</label>

                    <div class="controls">
                        <g:textArea name="comment" id="comment" value="${params.comment}"  rows="4" cols="40" />
                    </div>
                </div>

            </g:form>
        </div>
    </body>
</html>