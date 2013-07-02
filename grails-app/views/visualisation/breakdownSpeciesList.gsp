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
    <strong class="muted">${description}</strong>
    <div style="height: 380px; overflow-y: scroll">
        <table class="table table-striped table-bordered table-condensed">
            <g:each var="name" in="${nameList}">
                <tr>
                    <td><sts:taxaHomePageLink name="${name}"/></td>
                </tr>
            </g:each>
        </table>
    </div>
</div>

<script type="text/javascript">
    $("#modal_label_modal_element_id").html("${title}");
</script>