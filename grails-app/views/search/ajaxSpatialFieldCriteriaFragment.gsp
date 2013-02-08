<div>
    <div class="alert alert-info" style="color: black">
        <strong>${criteriaDefinition.description}</strong>
    </div>
    Please select one or more values from the list below
    <div style="margin-top: 5px; margin-bottom: 5px;">
        <g:select name="fieldValue" from="${allowedValues?.sort()}" multiple="multiple" size="12" style="width: 300px"/>
    </div>

</div>