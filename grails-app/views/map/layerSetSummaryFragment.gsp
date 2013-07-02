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
<div style="padding-bottom: 5px;">
    <table style="width: 100%">
        <tr>
            <td><h4 style="margin-top: 0px">${layerSet.name} <small>${layerSet.description}</small></h4></td>
            <td style="text-align: right; width: 350px">
                <span>
                    <g:checkBox style="margin-top: 0px" name="chkClearExisting"
                                id="chkClearExisting"/>&nbsp;Replace existing layers
                    <button id="btnAddLayerSet" class="btn btn-primary btn-small"><i
                        class="icon-plus icon-white"></i>&nbsp;Add Layers</button>
                    <sts:ifAdmin>
                        <button class="btn btn-small btn-warning" id="btnEditLayerSet">Edit</button>
                    </sts:ifAdmin>

                </span>
            </td>
        </tr>
    </table>
</div>

<div style="max-height: 170px; height:170px; overflow-y: scroll;">
    <table class="table table-condensed table-striped table-bordered">
        <tbody>
            <g:each in="${layerSet.layers}" var="layerName">
                <tr>
                    <td>${layerName}</td>
                    <td>${layerDescriptions[layerName]}</td>
                </tr>
            </g:each>
        </tbody>
    </table>
</div>
<script type="text/javascript">

    $("#btnAddLayerSet").click(function (e) {
        e.preventDefault();
        var replaceExisting = $("#chkClearExisting").prop('checked');
        addLayerSet(${layerSet.id}, replaceExisting);
    });

    $("#btnEditLayerSet").click(function (e) {
        e.preventDefault();
        <g:if test="${layerSet.user == null}">
        window.open("${createLink(controller: 'admin', action:'editLayerSet', id:layerSet.id)}", "UserPreferences");
        </g:if>
        <g:else>
        window.open("${createLink(controller:'userProfile', action:'editLayerSet', params:[layerSetId: layerSet.id])}", "UserPreferences");
        </g:else>
    });

</script>