<h5 style="margin-top: 0px; margin-bottom: 2px;">Selected Layer - ${layerDefinition.displayname}</h5>
<div>
  <table class="table table-bordered table-striped">
    <tr>
      <td>Description</td>
      <td>${layerDefinition.description}</td>
    </tr>
    <tr>
      <td>Classification</td>
      <td>
        <g:if test="${layerDefinition.classification1}">
          <span class="label">${layerDefinition.classification1}</span>
        </g:if>
        <g:if test="${layerDefinition.classification2}">
          <span class="label">${layerDefinition.classification2}</span>
        </g:if>
      </td>
    </tr>
  </table>
  <div class="container">
    <button class="btn btn-small btn-primary"><i class="icon-plus icon-white"></i>&nbsp;Add Layer</button>
  </div>
</div>