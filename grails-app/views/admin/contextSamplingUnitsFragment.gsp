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