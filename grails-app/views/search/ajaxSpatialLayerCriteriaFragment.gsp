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
    <div class="alert alert-info" style="color: black">
        <strong>${layerInfo.description}</strong>
        <div>
            <sts:criteriaValueControl criteriaDefinition="${criteriaDefinition}" units="${layerInfo?.units ?: layerInfo.environmentalvalueunits}" value="${criteria?.value}" />
        </div>
    </div>
    <g:if test="${layerInfo.notes}">
        <div class="well well-small" style="height: 180px; overflow-y: auto">
            <h5>Notes for ${layerInfo.displayname}</h5>
            ${layerInfo.notes}
        </div>
    </g:if>

</div>