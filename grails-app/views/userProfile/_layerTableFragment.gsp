<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<table class="table table-bordered table-striped table-condensed">
    <thead>
        <th>Layer Name</th>
        <th></th>
    </thead>
    <tbody>
        <g:each in="${layerSet.layers}" var="layerName">
            <tr layerName="${layerName}">
                <td>${StringEscapeUtils.escapeHtml(layerName)}</td>
                <td>
                    <button class="btn btn-mini btnShowLayerInfo"><i class="icon-info-sign"></i></button>
                    <button class="btn btn-mini btnRemoveLayerFromSet"><i class="icon-remove"></i></button>
                </td>
            </tr>
        </g:each>
    </tbody>
</table>
