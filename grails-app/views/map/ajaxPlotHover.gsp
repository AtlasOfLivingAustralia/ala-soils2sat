<div>
  <h5>Study location ${plotName}</h5>
  <g:if test="${plot}">
    <table>
      <tr>
        <td>Date</td>
        <td>${plot.date}</td>
      </tr>
      <tr>
        <td>Position</td>
        <td>${plot.longitude}, ${plot.latitude}</td>
      </tr>
    </table>
  </g:if>
  <g:else>
    Plot details could not be retrieved!
  </g:else>
</div>