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
    <img src="${createLink(controller:'studyLocation', action:'pointInterceptImage', params:[studyLocationVisitId: studyLocationVisitId, pointInterceptType: pointInterceptType])}" />
</div>
<div style="margin-top: 10px">
    <table class="table table-bordered" style="background-color: rgb(249, 249, 249)">
        <tr>
            <td width="50%">
                <div class="visualisation" visLink="${createLink(controller:'visualisation', action:'PICountComparison', params:[pointInterceptType: pointInterceptType, studyLocationVisitId: studyLocationVisitId])}"></div>
            </td>
            <td width="50%">
                <div class="visualisation" visLink="${createLink(controller:'visualisation', action:'PICountPercentBreakdown', params:[pointInterceptType: pointInterceptType, studyLocationVisitId: studyLocationVisitId])}"></div>
            </td>
        </tr>
        <tr>
            <td width="50%">
                <div class="visualisation" visLink="${createLink(controller:'visualisation', action:'PIFractionalCover', params:[studyLocationVisitId: studyLocationVisitId])}"></div>
            </td>
            <td width="50%">
                <div class="visualisation" visLink="${createLink(controller:'visualisation', action:'AusCoverFractionalCover', params:[studyLocationName: visitDetails.studyLocationName])}"></div>
            </td>
        </tr>
        <tr>
            <td width="50%">
                <div class="visualisation" visLink="${createLink(controller:'visualisation', action:'PIFractionalCoverAusPlotsVsAusCover', params:[studyLocationVisitId: studyLocationVisitId])}"></div>
            </td>
            <td width="50%">
                <div class="visualisation" visLink="${createLink(controller:'visualisation', action:'PIGroundCover', params:[studyLocationVisitId: studyLocationVisitId])}"></div>
            </td>
        </tr>

    </table>
</div>

<script>

    $(".visualisation").each(function() {
        var visLink =$(this).attr("visLink");
        if (visLink) {
            $(this).html('<img src="../images/spinner.gif" />&nbsp;Loading...');
            var targetElement = $("[visLink='" + visLink + "']");
            $.ajax(visLink).done(function(html) {
                targetElement.html(html);
            });
        }
    });

</script>