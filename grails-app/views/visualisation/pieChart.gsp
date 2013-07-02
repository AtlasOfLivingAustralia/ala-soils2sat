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

<g:if test="${data}">
    <g:set var="elementId" value="${java.util.UUID.randomUUID().toString()}" />
    <div class="visualisationContent" id="${elementId}">
        <sts:spinner />
    </div>

    <g:if test="${!colors}">
        <g:set var="colors" value="['#4E80BB', '#BD4E4C', '#98B856','#7E619E','#4AA8C2','#F39344','#3E6899','#9B403C','#7D9645','#685083','#3A8BA1','#C97A36']" />
    </g:if>

    <script type="text/javascript">

        function shimHandler${name}() {
            <g:if test="${selectHandler}">
                if (${selectHandler}) {
                    ${selectHandler}(${name}, "${modalContentLink}");
                }
            </g:if>
        }

    </script>

    <gvisualization:pieCoreChart dynamicLoading="true" name="${name}" elementId="${elementId}" columns="${columns}" data="${data}" title="${title}" colors="${colors}" is3D="${true}" select="shimHandler${name}" />

</g:if>
<g:else>
    <div class="muted noDataRecordedChart">
        <strong class="muted">${title}</strong>
        <div>
            No data recorded
        </div>
    </div>
</g:else>

