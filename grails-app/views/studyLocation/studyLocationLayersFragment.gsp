<div class="">
  <table class="table table-striped table-bordered">
    <thread>
      <tr>
        <th>Layer name</th>
        <th>Value at study location ${studyLocationName}</th>
      </tr>
    </thread>
    <g:each in="${layerData}" var="kvp">
      <tr>
        <td>${kvp.key}</td>
        <td>${kvp.value}</td>
      </tr>
    </g:each>
  </table>
</div>