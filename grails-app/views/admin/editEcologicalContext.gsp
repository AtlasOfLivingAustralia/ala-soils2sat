<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Ecological Contexts</title>
    </head>

    <body>
        <content tag="pageTitle">Ecological Contexts</content>
        <content tag="adminButtonBar">
        </content>

        <content tag="adminButtonBar">
            <a class="btn btn-small" href="${createLink(controller: 'admin', action:'ecologicalContexts')}">Back to contexts</a>
        </content>
        <g:form class="form-horizontal" action="updateEcologicalContext">
            <div class="well well-small">
                <h5>Edit Ecological Context</h5>

                <g:hiddenField name="ecologicalContextId" value="${context.id}" />

                <div class="control-group">
                    <label class="control-label" for='name'>Name:</label>
                    <div class="controls">
                        <g:textField class="input-xlarge" name="name" placeholder="Name" value="${context.name}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='description'>Description:</label>

                    <div class="controls">
                        <g:textField class="input-xlarge" name="description" placeholder="Description" value="${context.description}"/>
                    </div>
                </div>

                <div class="control-group">
                    <div class="controls">
                        <g:submitButton class="btn btn-primary" name="submit" value='Update'/>
                    </div>
                </div>

            </div>

        </g:form>

        <div class="well well-small">
            <h5>Sampling Units associated with this context</h5>
            <div class="control-group">
                <div class="controls">
                    <g:select id="samplingUnit" from="${samplingUnits}" optionKey="id" optionValue="name" noSelection="['':'- Choose a sampling unit -']" style="margin-bottom: 0" name="unit" class="input-xlarge"/>
                    <button id="btnAddUnit" class="btn btn-primary"><i class="icon-plus icon-white"></i>&nbsp;Add</button>
                    <button type="button" id="btnRemoveAll" class="btn btn-danger pull-right"><i class="icon-remove icon-white"></i>&nbsp;Remove All</button>
                </div>
            </div>
            <div id="samplingUnits"></div>
        </div>
        <script type="text/javascript">

            $(document).ready(function(e) {

                $("#btnAddUnit").click(function(e) {
                    e.preventDefault();
                    var value = $("#samplingUnit").val();
                    if (value) {
                        var url = "${createLink(controller:'admin', action:'addContextSamplingUnitAjax', params:[ecologicalContextId: context.id])}&samplingUnitId=" + value;
                        $.ajax(url).done(function() {
                            renderSamplingUnits();
                        });
                    }
                });

                $("#btnRemoveAll").click(function(e) {
                    e.preventDefault();
                    if (confirm("Are you sure you wish to remove all sampling units from this ecological context?")) {
                        window.location = "${createLink(action:'removeAllContextSamplingUnits', params:[ecologicalContextId: context.id])}";
                    }
                });

                renderSamplingUnits();
            });

            function renderSamplingUnits() {
                $("#samplingUnits").html("Loading...");
                $.ajax("${createLink(controller:'admin', action:'contextSamplingUnitsFragment', params:[ecologicalContextId:context.id])}").done(function(content) {
                    $("#samplingUnits").html(content);
                });
            }

        </script>
    </body>
</html>
