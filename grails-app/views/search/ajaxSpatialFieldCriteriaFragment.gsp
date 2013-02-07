<div>
    <div class="well well-small">
        ${criteriaDefinition.description}
    </div>
    Please select one or more values from the list below
    <div style="margin-top: 5px; margin-bottom: 5px;">
        <g:select name="fieldValue" from="${allowedValues?.sort()}" multiple="multiple" size="12" style="width: 300px"/>
    </div>

</div>