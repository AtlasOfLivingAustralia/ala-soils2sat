<style type="text/css">

    .visualisationContent {
        height: 300px;
    }

</style>

<h4>Visualisations</h4>

<table class="table table-striped">
    <tr>
        <td width="50%">
            <div class="visualisation" visLink="${createLink(action:'plantSpeciesBreakdownByLocation', params:[studyLocationName: studyLocationName, random: UUID.randomUUID().toString()])}"></div>
        </td>
        <td width="50%">
            <div class="visualisation" visLink="${createLink(action:'soilpHForLocation', params:[studyLocationName: studyLocationName, random: UUID.randomUUID().toString()])}"></div>
        </td>
    </tr>
    <tr>
        <td>
            <div class="visualisation" visLink="${createLink(action:'soilECForLocation', params:[studyLocationName: studyLocationName, random: UUID.randomUUID().toString()])}"></div>
        </td>
        <td>
            <div class="visualisation" visLink="${createLink(action:'weedNonWeedBreakdownForLocation', params:[studyLocationName: studyLocationName, random: UUID.randomUUID().toString()])}"></div>
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