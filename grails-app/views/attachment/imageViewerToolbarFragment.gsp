<style type="text/css">
    .imageTool {
        margin-right: 5px;
    }
</style>

<table style="width: 100%">
    <tr>
        <td><h3>${attachment?.name}</h3></td>
        <td style="min-width: 320px">
            <a id="btnDownload" class="btn btn-info btn-small imageTool" title="Download image" href="#"><i class="icon-download icon-white"></i></a>
            <a id="btnZoomIn" class="btn btn-small imageTool" title="Zoom in" href="#"><i class="icon-zoom-in"></i></a>
            <a id="btnZoomOut" class="btn btn-small imageTool" title="Zoom out" href="#"><i class="icon-zoom-out"></i></a>
            <a id="btnPanRight" class="btn btn-small imageTool" title="Pan left" href="#"><i class="icon-arrow-left"></i></a>
            <a id="btnPanLeft" class="btn btn-small imageTool" title="Pan right" href="#"><i class="icon-arrow-right"></i></a>
            <a id="btnPanDown" class="btn btn-small imageTool" title="Pan up" href="#"><i class="icon-arrow-up"></i></a>
            <a id="btnPanUp" class="btn btn-small imageTool" title="Pan down" href="#"><i class="icon-arrow-down"></i></a>
        </td>
        <td><button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button></td>
    </tr>
</table>

<script type="text/javascript">

    $("#btnDownload").click(function(e) {
        e.preventDefault();
        window.location = "${createLink(controller:'attachment', action:'download', id: attachment.id)}";
    });

</script>
