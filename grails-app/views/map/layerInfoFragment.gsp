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
    <div style="height: 430px; overflow-y: scroll;">
        <table class="table table-striped table-bordered">
            <tr>
                <td>Display Name</td>
                <td>${layerInfo.displayname}</td>
            </tr>
            <tr>
                <td>Description</td>
                <td>${layerInfo.description}</td>
            </tr>
            <tr>
                <td>Classifications</td>
                <td>
                    <g:if test="${layerInfo.classification1}">
                        <span class="label">${layerInfo.classification1}</span>
                    </g:if>
                    <g:if test="${layerInfo.classification2}">
                        <span class="label">${layerInfo.classification2}</span>
                    </g:if>
                </td>
            </tr>
            <tr>
                <td>Keywords</td>
                <td>
                    <g:if test="${layerInfo.keywords}">
                        <g:each in="${layerInfo.keywords.split(',')}" var="keyword">
                            <span class="label label-info">${keyword}</span>
                        </g:each>
                    </g:if>
                </td>
            </tr>
            <tr>
                <td>Type</td>
                <td>${layerInfo.type}</td>
            </tr>
            <tr>
                <td>Notes</td>
                <td>${layerInfo.notes}</td>
            </tr>
            <g:set var="suppressList" value="['displayname', 'classification1', 'classification2', 'keywords', 'description', 'notes', 'type']"/>
            <g:each in="${layerInfo.sort { it.key }}" var="kvp">
                <g:if test="${!suppressList.contains(kvp.key)}">
                    <tr>
                        <td>${kvp.key}</td>
                        <td>
                            <sts:layerMetadataValue key="${kvp.key}" value="${kvp.value}" />
                        </td>
                    </tr>
                </g:if>
            </g:each>
        </table>
    </div>

</div>
