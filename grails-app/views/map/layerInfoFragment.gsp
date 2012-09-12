<div>
  <h4>Layer Information - ${layerName}</h4>
  <div style="height: 400px; overflow-y: scroll;">
    <table class="table table-striped table-bordered">
      <g:each in="${layerInfo}" var="kvp">
        <tr>
          <td>${kvp.key}</td>
          <td>${kvp.value}</td>
        </tr>
      </g:each>
    </table>
  </div>
</div>