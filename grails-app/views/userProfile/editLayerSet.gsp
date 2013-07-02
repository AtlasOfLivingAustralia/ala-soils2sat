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

<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="profilePage"/>
        <title>Your layer sets</title>
    </head>
    <body>
        <content tag="pageTitle">Layer Sets</content>

        <content tag="profileButtonBar">
            <button id="btnReturnToLayerSets" class="btn btn-small pull-right">Return to layer sets</button>
        </content>

        <div class="tabbable">

            <ul class="nav nav-tabs" style="margin-bottom: 0px">
                <li class="active"><a href="#detailsTab" data-toggle="tab">Details</a></li>
                <li><a href="#layersTab" data-toggle="tab">Layers</a></li>
            </ul>

            <div class="tab-content">
                <div class="tab-pane active" id="detailsTab">
                    <g:form class="form-horizontal" action="updateLayerSet" controller="userProfile"
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
                </div>
                <div class="tab-pane" id="layersTab">
                    <div class="well well-small">
                        <h5>Add layer to set</h5>
                        <input id="layer" placeholder="Search..." class="ui-autocomplete-input" autocomplete="off"
                               role="textbox"
                               aria-autocomplete="list" aria-haspopup="true" style="width:400px; margin-bottom: 10px">
                        <br/>

                        <div id="layerInfoPanel" style="margin-top:10px; height: 150px"></div>
                    </div>

                    <h5>Layers</h5>
                    <div id="layerTableContent">
                        <g:render template="layerTableFragment" params="${[layerSet:layerSet]}" />
                    </div>
                </div>
            </div>
        </div>

        <r:script>

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
                var url = "${createLink(controller:'userProfile', action:'ajaxAddLayerToLayerSet')}?layerSetId=${layerSet.id}&layerName=" + encodeURIComponent(layerName);
                $.ajax(url).done(function(html) {
                    $("#layerTableContent").html(html);
                    applyLayerTableEventHandlers();
                });
            }

            $("#btnReturnToLayerSets").click(function (e) {
                e.preventDefault();
                window.location = "${createLink(controller: 'userProfile',action:'listLayerSets')}";
            });

            function applyLayerTableEventHandlers() {

                $(".btnShowLayerInfo").click(function (e) {
                    e.preventDefault();
                    var layerName = $(this).parents("tr[layerName]").attr("layerName");
                    if (layerName) {
                        displayLayerInfo(layerName);
                    }
                });

                $(".btnRemoveLayerFromSet").click(function (e) {
                    e.preventDefault();
                    var layerName = $(this).parents("tr[layerName]").attr("layerName");
                    if (layerName) {
                        if (confirm("Remove layer '" + layerName + "' from layer set ${layerSet.name}?")) {
                            $.ajax("${createLink(controller:'userProfile', action:'ajaxRemoveLayerFromLayerSet', params:[layerSetId: layerSet.id])}&layerName=" + layerName).done(function(html) {
                                $("#layerTableContent").html(html);
                                applyLayerTableEventHandlers();
                            });
                        }
                    }
                });
            }

            applyLayerTableEventHandlers();

        </r:script>
    </body>
</html>

