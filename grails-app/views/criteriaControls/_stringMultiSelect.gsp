<div>
    <div class="alert alert-info" style="color: black">
        <strong>${criteriaDefinition.description}</strong>
    </div>
    Please select one or more values from the list below
    <table>
        <tr>
            <td>
                <div style="margin-top: 5px; margin-bottom: 5px;">
                    <g:select id="fieldListBox" name="fieldValue" from="${allowedValues?.sort({ it.value })}" optionValue="value" optionKey="name" multiple="multiple" size="12" style="width: 300px" value="${criteria?.value?.split("\\|")?.toList()}" />
                </div>
            </td>
            <td style="vertical-align: top">
                <div style="margin-left: 10px" class="muted">
                    <strong>Selected values</strong>
                    <div id="selectedItemsList">
                    </div>
                </div>
            </td>
        </tr>
    </table>

</div>

<script type="text/javascript">

    var listBox = $("#fieldListBox");

    listBox.change(function(e) {
        renderSelectedItemsList();
    });

    function renderSelectedItemsList() {
        var html = "<ul>"

        var values = $("#fieldListBox").val();
        if (values) {
            for (itemIndex in values) {
                var item = values[itemIndex]
                html += "<li>" + item + "</li>";
            }
        }
        html += "</ul>"
        $("#selectedItemsList").html(html);
    }

    renderSelectedItemsList();
    listBox.focus();
</script>
