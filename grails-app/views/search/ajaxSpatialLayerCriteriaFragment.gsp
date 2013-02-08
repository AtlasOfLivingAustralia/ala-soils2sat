<div>
    <div class="alert alert-info" style="color: black">
        <strong>${layerInfo.description}</strong>
        <div>
            <sts:criteriaValueControl criteriaDefinition="${criteriaDefinition}" units="${layerInfo?.units ?: layerInfo.environmentalvalueunits}"/>
        </div>
    </div>
    <g:if test="${layerInfo.notes}">
        <div class="well well-small" style="height: 180px; overflow-y: auto">
            <h5>Notes for ${layerInfo.displayname}</h5>
            ${layerInfo.notes}
        </div>
    </g:if>

</div>