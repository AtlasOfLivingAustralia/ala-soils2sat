<div>
    <div class="well well-small">
        ${layerInfo.description}
    </div>
    <div style="margin-top: 5px; margin-bottom: 5px;">
        <span>
            <sts:criteriaValueControl criteriaDefinition="${criteriaDefinition}" units="${layerInfo?.units ?: layerInfo.environmentalvalueunits}"/>
        </span>
    </div>
    <g:if test="${layerInfo.notes}">
        <div class="well well-small" style="height: 215px; overflow-y: auto">
            <h5>Notes for ${layerInfo.displayname}</h5>
            ${layerInfo.notes}
        </div>
    </g:if>

</div>