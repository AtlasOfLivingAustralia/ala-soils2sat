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

<g:if test="${context?.samplingUnits}">
        <table class="table table-bordered table-striped table-condensed">
            <g:each in="${context?.samplingUnits?.sort({ it.name })}" var="samplingUnit">
                <tr samplingUnitId="${samplingUnit.id}">
                    <td>${samplingUnit.name}</td>
                    <td><button class="btn btn-mini btn-danger btnRemoveSamplingUnit"><i class="icon-remove icon-white"></i>&nbsp;delete</button></td>
                </tr>
            </g:each>
        </table>
    </g:if>
    <g:else>
        <div class="alert alert-info">
            No sampling units have been defined for this context yet
        </div>
    </g:else>
</div>

<script type="text/javascript">

    $(".btnRemoveSamplingUnit").click(function(e) {
        e.preventDefault();
        var samplingUnitId = $(this).parents("[samplingUnitId]").attr("samplingUnitId");
        if (samplingUnitId) {
            var url = "${createLink(controller:'admin',action:'removeContextSamplingUnitAjax', params:[ecologicalContextId:context.id])}&samplingUnitId=" + samplingUnitId;
            $.ajax(url).done(function() {
                renderSamplingUnits();
            });
        }

    });
</script>