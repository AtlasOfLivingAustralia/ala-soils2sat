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

<div class="">
    Vouchered specimens recorded at ${studyLocationDetails.studyLocationName}
    <table class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>Species</th>
            </tr>
        </thead>
        <g:each in="${ausplotsNames}" var="taxon">
            <g:set var="isWeed" value="${weeds.find { it.toLowerCase().equalsIgnoreCase(taxon.toLowerCase()) } }" />
            <tr class="${isWeed != null ? 'warning' : '' }">
                <td><sts:taxaHomePageLink name="${taxon}"/></td>
            </tr>
        </g:each>
    </table>

    Taxa list derived from specimen data collected within ${radius} kilometers of ${studyLocationDetails?.studyLocationName} (${studyLocationDetails.longitude}, ${studyLocationDetails.latitude})
    <table class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>${rank}</th>
            </tr>
        </thead>
        <g:each in="${alaNames}" var="taxon">
            <g:set var="isWeed" value="${weeds.find { it.toLowerCase().equalsIgnoreCase(taxon.toLowerCase()) } }" />
            <tr class="${isWeed != null ? 'warning' : '' }">
                <td><sts:taxaHomePageLink name="${taxon}"/></td>
            </tr>
        </g:each>
    </table>
</div>