<div>
  <div style="height: 430px; overflow-y: scroll;">
    <table class="table table-striped table-bordered">
      <tr>
        <td>Display Name</td>
        <td>${layerInfo.displayname}</td>
      </tr>
      <tr>
        <td>Description</td>
        <td>${layerInfo.description}</td>
      </tr>
      <tr>
        <td>Classifications</td>
        <td>
          <g:if test="${layerInfo.classification1}">
            <span class="label">${layerInfo.classification1}</span>
          </g:if>
          <g:if test="${layerInfo.classification2}">
            <span class="label">${layerInfo.classification2}</span>
          </g:if>
        </td>
      </tr>
      <tr>
        <td>Keywords</td>
        <td>
          <g:if test="${layerInfo.keywords}">
            <g:each in="${layerInfo.keywords.split(',')}" var="keyword">
              <span class="label label-info">${keyword}</span>
            </g:each>
          </g:if>
        </td>
      </tr>
      <tr>
        <td>Type</td>
        <td>${layerInfo.type}</td>
      </tr>
      <tr>
        <td>Notes</td>
        <td>${layerInfo.notes}</td>
      </tr>
      <g:set var="suppressList" value="['displayname','classification1','classification2', 'keywords', 'description', 'notes','type']"/>
      <g:each in="${layerInfo.sort { it.key }}" var="kvp">
        <g:if test="${!suppressList.contains(kvp.key)}">
          <tr>
            <td>${kvp.key}</td>
            <td>${kvp.value}</td>
          </tr>
        </g:if>
      </g:each>
    </table>
  </div>

  <div style="margin-top: 15px;">
    <button id="btnCloseLayerInformation" class="btn btn-small pull-right">Close</button>
  </div>

</div>
<script type="text/javascript">

  $("#btnCloseLayerInformation").click(function(e) {
    e.preventDefault();
//    $.fancybox.close();
  });

</script>