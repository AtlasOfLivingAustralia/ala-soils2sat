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
