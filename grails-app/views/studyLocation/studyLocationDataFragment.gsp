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
    <div style="height: 250px; overflow-y: scroll">
        <table class="table table-bordered table-striped">
            <tr>
                <td>Location (Lat, Long)</td>
                <td>${studyLocation.longitude}, ${studyLocation.latitude}</td>
            </tr>
            <tr>
                <td>Location (UTM)</td>
                <td>${studyLocation.data.easting}, ${studyLocation.data.northing} (Zone ${studyLocation.data.zone as Integer})</td>
            </tr>
            <tr>
                <td>Bioregion Name</td>
                <td>${studyLocation.data.bioregionName}</td>
            </tr>
            <tr>
                <td>Landform Pattern</td>
                <td>${studyLocation.data.landformPattern}</td>
            </tr>
            <tr>
                <td>Landform Element</td>
                <td>${studyLocation.data.landformElement}</td>
            </tr>
            <tr>
                <td>Visits (${studyLocation.data.numVisits})</td>
                <td>Initial visit date: <sts:formatDateStr date="${studyLocation.firstVisitDate}"/><br/>
                    Lastest visit date: <sts:formatDateStr date="${studyLocation.lastVisitDate}"/>
                </td>
            </tr>
            <tr>
                <td>Sampling Methods performed<br/>at this site location</td>
                <td>
                    <ul>
                        <g:each in="${studyLocation.data.samplingUnitTypeList}" var="unit">
                            <li><sts:formatSamplingUnitColumn code="${unit}"/></li>
                        </g:each>
                    </ul>
                </td>
            </tr>
        </table>
    </div>
</div>