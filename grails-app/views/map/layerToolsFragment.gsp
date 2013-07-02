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
    <h5>${layerInfo.displayname}</h5>
    <table class="table table-condensed">
        <tr>
            <td>
                <img src="http://spatial.ala.org.au/geoserver/wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH=20&HEIGHT=5&LAYER=${layerName}" alt="Layer legend"/>
            </td>
            <td style="text-align: center">
                <div style="margin-bottom: 10px"><small>Opacity</small></div>

                <div id="opacitySlider" style="margin-left: auto; margin-right: auto"></div>

                <div style="margin-top: 10px" id="opacityValue"><small>${Math.round((layerInstance.opacity ?: 1.0) * 100)}%</small></div>
            </td>
        </tr>
    </table>
</div>

<script type="text/javascript">
    var startingOpacity =
    ${layerInstance.opacity ?: 1.0} *
    100;
    $("#opacitySlider").slider({
        min: 0,
        max: 100,
        value: startingOpacity,
        orientation: "vertical",
        change: function (event, ui) {
            $("#opacityValue").html(ui.value + "%");
            setLayerOpacity('${layerName}', ui.value / 100);
        },
        slide: function (event, ui) {
            $("#opacityValue").html("<small>" + ui.value + "%</small>");
        }

    });

</script>