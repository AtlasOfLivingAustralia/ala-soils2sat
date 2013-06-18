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