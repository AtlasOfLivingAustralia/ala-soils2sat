<div>
    <div>
        <g:if test="${value}">
            <g:set var="evaluator" value="${new au.org.ala.soils2sat.SearchCriteriaUtils.DoubleCriteriaEvaluator(value as String)}" />
        </g:if>
        <label class="radio inline" style="white-space: nowrap">
            <g:radio class="radioButton" name="operator" value="gt" checked="${evaluator == null || evaluator?.operator == 'gt' ? 'checked' : ''}"/>
            Greater than or equal to
        </label>
        <label class="radio inline" style="white-space: nowrap">
            <g:radio class="radioButton" name="operator" value="lt" checked="${evaluator?.operator == 'lt' ? 'checked' : ''}" />
            Less than or equal to
        </label>
        <label class="radio inline" style="white-space: nowrap">
            <g:radio class="radioButton" name="operator" value="bt" checked="${evaluator?.operator == 'bt' ? 'checked' : ''}" />
            Between
        </label>
    </div>
    <div style="margin-top: 15px">
        <g:textField class="input-small" style="margin-left: 20px" name="numberValue" placeholder="Value" value="${evaluator?.value1}"/>
        <span class="numberRangeOther" style="display: ${evaluator?.operator == 'bt' ? 'inline-block' : 'none' }">
            &nbsp;and&nbsp;
            <g:textField class="input-small" style="margin-left: 20px" name="numberValue2" placeholder="Value" value="${evaluator?.value2}"/>
        </span>
        <g:if test="${units}">
            <span>&nbsp;(${units})</span>
        </g:if>

    </div>
    <g:hiddenField name="units" value="${units}" />
</div>
<script type="text/javascript">

    $(document).ready(function() {

        $(".radioButton").click(function(e) {
            var operator = $(this).val();
            if (operator == "bt") {
                $(".numberRangeOther").css("display", "inline-block");
            } else {
                $(".numberRangeOther").css("display", "none");
            }
        });

    });

</script>
