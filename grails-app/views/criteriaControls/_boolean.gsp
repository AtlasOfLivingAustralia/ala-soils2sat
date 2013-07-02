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
    <div class="well well-small">
        ${criteriaDefinition.description}
        <div>
            <label class="radio inline" style="white-space: nowrap">
                <g:radio class="radioButton" name="value" value="true" checked="${value == null || value=='true' ? 'checked': '' }"/>
                Yes
            </label>
            <label class="radio inline" style="white-space: nowrap">
                <g:radio class="radioButton" name="value" value="lt" checked="${value == false ? 'checked' : ''}" />
                No
            </label>
        </div>
    </div>

</div>
<script type="text/javascript">

    $(document).ready(function() {

    });

</script>
