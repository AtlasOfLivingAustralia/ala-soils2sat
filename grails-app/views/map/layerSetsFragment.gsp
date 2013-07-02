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
    <div id="layerSetTree" style="max-height: 160px; height: 160px; overflow-y: scroll;">
        <ul item-selected="true">
            <li><b>Predefined Layer Sets</b>
                <ul>
                    <g:each in="${globalLayerSets}" var="layerSet">
                        <li layerSetId="${layerSet.id}">${layerSet.name} (${layerSet.layers.size()} layers)
                        </li>
                    </g:each>
                </ul>
            </li>
            <li><b>User Defined Layer Sets</b>&nbsp;<button id="btnEditUserLayerSets" class="btn btn-mini">Edit layer sets</button>
                <ul>
                    <g:if test="${userLayerSets}">
                        <g:each in="${userLayerSets}" var="layerSet">
                            <li layerSetId="${layerSet.id}">${layerSet.name} (${layerSet.layers.size()} layers)
                            </li>
                        </g:each>
                    </g:if>
                    <g:else>
                        <li>No user defined layer sets found</li>
                    </g:else>
                </ul>
            </li>
        </ul>
    </div>
</div>

<div class="well well-small" style="margin-top: 10px; margin-bottom: 0px; height:210px;">
    <div id="layerSetInfo">
    </div>
</div>

<script type="text/javascript">

    var theme = "ui-smoothness";

    $("#layerSetTree").jqxTree({
        theme:theme
    }).bind('select', onLayerSetSelect);

    function onLayerSetSelect(event) {
        var layerSetId = $(event.args.element).attr("layerSetId");
        if (layerSetId) {
            $("#layerSetInfo").html("Retrieving layer set information... <sts:spinner/>");
            $.ajax("${createLink(controller:'map', action:'layerSetSummaryFragment')}?layerSetId=" + layerSetId).done(function (content) {
                $("#layerSetInfo").html(content);
            });
        } else {
            $("#layerSetInfo").html("");
        }
    }

    $("#btnEditUserLayerSets").click(function (e) {
        e.preventDefault();
        window.open("${createLink(controller:'userProfile', action:'listLayerSets')}", "UserPreferences");
    });

</script>