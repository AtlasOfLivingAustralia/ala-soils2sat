<style type="text/css">

    .visualisationContent {
        height: 300px;
    }

</style>

<h4>Visualisations</h4>

<table class="table table-bordered" style="background-color: rgb(249, 249, 249)">
    <tr>
        <td colspan="2">
            <div class="visualisation" visLink="${createLink(action:'compareDistinctSpecies')}"></div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div class="visualisation" visLink="${createLink(action:'compareScalarLayer', params:[layerName:'rain_ann'])}"></div>
        </td>
    </tr>
    <tr>
        <td width="50%">
            <div class="visualisation" visLink="${createLink(action:'compareLandformElement')}"></div>
        </td>
        <td width="50%">
        </td>
    </tr>
</table>

<script type="text/javascript">

    $(".visualisation").each(function() {
        var visLink =$(this).attr("visLink");
        if (visLink) {
            var targetElement = $("[visLink='" + visLink + "']");

            targetElement.html("Loading...");
            $.ajax(visLink).done(function(html) {
                targetElement.html(html);
            });

        }
    });

</script>