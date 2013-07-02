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
    <h5>Study location ${studyLocationName}</h5>
    <g:if test="${studyLocation}">
        <table>
            <tr>
                <td><strong>Visit&nbsp;dates</strong></td>
                <td><sts:formatDateStr date="${studyLocation.firstVisitDate}"/> - <sts:formatDateStr date="${studyLocation.lastVisitDate}"/></td>
            </tr>
            <tr>
                <td><strong>Position</strong></td>
                <td>${studyLocation.longitude}, ${studyLocation.latitude}</td>
            </tr>
            <tr>
                <td style="vertical-align: top"><strong>Sampling&nbsp;Units</strong></td>
                <td style="vertical-align: top">${studyLocation.samplingUnits?.collect({ it.description })?.join(", ")}</td>
            </tr>


        </table>
    </g:if>
    <g:else>
        Study location details could not be retrieved!
    </g:else>
</div>