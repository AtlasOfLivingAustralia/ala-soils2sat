<div style="overflow: scroll">
    <img src="${createLink(controller:'attachment', action:'download', id: attachment.id)}" />
</div>

<script type="text/javascript">
    $("#modal_label_modal_element_id").html("${attachment.name}");
</script>