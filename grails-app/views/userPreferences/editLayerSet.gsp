<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="detail"/>
        <title>Your layer sets</title>
    </head>
    <body>
        <div class="container">


            <legend>
                <table style="width:100%">
                    <tr>
                        <td>Edit Layer Set</td>
                        <td><button id="btnReturnToLayerSets" class="btn btn-small pull-right">Return to layer sets</button>
                        </td>
                    </tr>
                </table>
            </legend>

            <g:form class="form-horizontal" action="updateLayerSet" controller="userPreferences"
                    params="${[layerSetId: layerSet.id]}">

                <div class="control-group">
                    <label class="control-label" for='name'>Name:</label>

                    <div class="controls">
                        <g:textField name="name" placeholder="Name" value="${layerSet?.name}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for='name'>Description:</label>

                    <div class="controls">
                        <g:textField class="input-xlarge" name="description" placeholder="Description"
                                     value="${layerSet?.description}"/>
                    </div>
                </div>

                <div class="control-group">
                    <div class="controls">
                        <g:submitButton class="btn btn-primary" name="submit" value='Update'/>
                    </div>
                </div>

            </g:form>

            <div class="row">
                <div class="span12">

                    <div class="well well-small">
                        <h5>Add layer to set</h5>
                        <input id="layer" placeholder="Search..." class="ui-autocomplete-input" autocomplete="off"
                               role="textbox"
                               aria-autocomplete="list" aria-haspopup="true" style="width:400px; margin-bottom: 10px">
                        <br/>

                        <div id="layerInfoPanel" style="margin-top:10px; height: 130px"></div>
                    </div>

                    <h5>Layers</h5>
                    <table class="table table-bordered table-striped table-condensed">
                        <thead>
                        <th>Layer Name</th>
                        <th></th>
                        </thead>
                        <tbody>
                        <g:each in="${layerSet.layers}" var="layerName">
                            <tr layerName="${layerName}">
                                <td>${StringEscapeUtils.escapeHtml(layerName)}</td>
                                <td>
                                    <button class="btn btn-mini btnShowLayerInfo"><i class="icon-info-sign"></i></button>
                                    <button class="btn btn-mini btnRemoveLayerFromSet"><i class="icon-remove"></i></button>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
        <r:script>

            $(".btnShowLayerInfo").click(function (e) {
                e.preventDefault();
                var layerName = $(this).parents("tr[layerName]").attr("layerName");
                if (layerName) {
                    displayLayerInfo(layerName);
                }
            });

            function displayLayerInfo(layerName) {

                showModal({
                   title:'Layer details - ' + layerName,
                   height: 500,
                   width: 800,
                   url: "${createLink(controller:'map', action:'layerInfoFragment')}?layerName=" + layerName
                });

                return true;
            }

            $(".btnRemoveLayerFromSet").click(function (e) {
                e.preventDefault();
                var layerName = $(this).parents("tr[layerName]").attr("layerName");
                if (layerName) {
                    if (confirm("Remove layer '" + layerName + "' from layer set ${layerSet.name}?")) {
                        window.location = "${createLink(controller:'userPreferences', action:'removeLayerFromLayerSet', params:[layerSetId: layerSet.id])}&layerName=" + layerName;
                    }
                }

            });

            $("#layer").autocomplete({
                source:function (request, response) {
                    $("#layerInfoPanel").html("");
                    $.ajax({
                        url:"${createLink(controller: 'spatialProxy', action:'layersSearch')}",
                        dataType:"json",
                        data:{
                            q:request.term
                        },
                        success:function (data) {
                            response($.map(data, function (item) {
                                return {
                                    label:item.displayname,
                                    value:item.displayname,
                                    id:item.uid,
                                    name:item.name,
                                    description:item.description,
                                    licence:item.licence_notes,
                                    classification1:item.classification1,
                                    classification2:item.classification2
                                }
                            }));
                        },
                        error:function (jqXHR, textStatus, errorThrown) {
                            alert("Unable to complete request.\n" + errorThrown);
                        }
                    });
                },
                minLength:3,
                html:true,
                select:function (event, ui) {
                    var item = ui.item;
                    $("#layerInfoPanel").html("");
                    $.ajax("${createLink(controller: 'map', action:'layerSummaryFragment')}?layerName=" + item.name + "&callback=addLayerToSet&showInfoButton=true").done(function (content) {
                        $("#layerInfoPanel").html(content);
                    });
                }
            });

            function addLayerToSet(layerName) {
                var url = "${createLink(controller:'userPreferences', action:'addLayerToLayerSet')}?layerSetId=${layerSet.id}&layerName=" + encodeURIComponent(layerName);
                window.location = url;
            }

            $("#btnReturnToLayerSets").click(function (e) {
                e.preventDefault();
                window.location = "${createLink(controller: 'userPreferences',action:'listLayerSets')}";
            });

        </r:script>
    </body>
</html>

