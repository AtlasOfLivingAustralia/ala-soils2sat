<div>
    <strong class="muted">
        ${description}
    </strong>
    <div style="height: 380px; overflow-y: scroll">
        <table class="table table-striped table-bordered table-condensed">
            <g:each var="name" in="${nameList}">
                <tr>
                    <td><sts:taxaHomePageLink name="${name}"/></td>
                </tr>
            </g:each>
        </table>
    </div>
</div>

<script type="text/javascript">
    $("#modal_label_modal_element_id").html("${title}");
</script>