<div>
  <div style="height: 200px; overflow-y: scroll">
    <table class="table table-bordered table-striped">
      <g:each in="${results}" var="result">
        <tr>
          <td>${result.layername ?: result.field}</td>
          <td>${result.value}</td>
        </tr>
      </g:each>
    </table>
  </div>
</div>