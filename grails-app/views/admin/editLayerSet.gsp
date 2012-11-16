<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
<head>
    <r:require module='jqueryui'/>
    <r:require module='bootstrap_responsive'/>
    <r:require module='fancybox'/>
    <meta name="layout" content="adminLayout"/>
    <title>Soils to Satellites - Admin - Edit layer set</title>
</head>

<body>
<content tag="pageTitle">Edit Layer Set - ${layerSet?.name}</content>
<g:form class="form-horizontal" action="saveLayerSet" controller="admin" id="${layerSet?.id}">

    <div class="well well-small">
        <h5>Layer Set Details</h5>

        <div class="control-group">
            <label class="control-label" for='name'>Name:</label>

            <div class="controls">
                <g:textField name="name" placeholder="Name" value="${layerSet?.name}"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for='description'>Description:</label>
            <div class="controls">
                <g:textField class="input-xlarge" name="description" placeholder="Description" value="${layerSet?.description}"/>
            </div>
        </div>

        <div class="control-group">
            <div class="controls">
                <g:submitButton class="btn btn-primary" name="submit" value='Update'/>
            </div>
        </div>
    </div>

    <div class="well well-small">
        <h5>Add layer to set</h5>
        <input id="layer" placeholder="Search..." class="ui-autocomplete-input" autocomplete="off" role="textbox"
               aria-autocomplete="list" aria-haspopup="true" style="width:400px; margin-bottom: 10px">
        <br/>

        <div id="layerInfoPanel" style="margin-top:10px; height: 180px"></div>
    </div>
    <h5>Layers</h5>
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
                    <button class="btn btn-mini btnRemoveLayerFromSet"><i class="icon-trash"></i></button>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
</g:form>

<a id="layerInfoLink" href="#layerInfo" style="display: none"></a>

<div id="layerInfo" style="display:none; width: 600px; height: 500px">
    <div id="layerInfoContent">
    </div>
</div>

<r:script type="text/javascript">

    $(document).ready(function () {

        %{--$("#layerInfoLink").fancybox({--}%
            %{--beforeLoad:function () {--}%
                %{--var layerName = $("#layerInfoContent").attr("layerName");--}%
                %{--$("#layerInfoContent").html("Loading...");--}%
                %{--$.ajax("${createLink(controller: 'map', action:'layerInfoFragment')}?layerName=" + layerName).done(function (html) {--}%
                    %{--$("#layerInfoContent").html(html);--}%
                %{--});--}%
            %{--}--}%
        %{--});--}%

        $(".btnShowLayerInfo").click(function (e) {
            e.preventDefault();
            var layerName = $(this).parents("tr[layerName]").attr("layerName");
            if (layerName) {
                displayLayerInfo(layerName);
            }
        });

        function displayLayerInfo(layerName) {
            showModal({
                url: "${createLink(controller: 'map', action:'layerInfoFragment')}?layerName=" + layerName,
                title: "Layer details - " + layerName,
                height: 500,
                width: 600
            });
            %{--$("#layerInfoContent").attr("layerName", layerName);--}%
            %{--$("#layerInfoLink").click();--}%
            return true;
        }

        $(".btnRemoveLayerFromSet").click(function (e) {
            e.preventDefault();
            var layerName = $(this).parents("tr[layerName]").attr("layerName");
            if (layerName) {
                if (confirm("Remove layer '" + layerName + "' from layer set ${layerSet.name}?")) {
                    window.location = "${createLink(controller:'admin', action:'removeLayerFromLayerSet',id:layerSet.id)}?layerName=" + layerName;
                }
            }

        });

        $("#layer").autocomplete({
            source:function (request, response) {
                $("#layerInfoPanel").html("");
                $.ajax({
                    url:"${createLink(controller: 'spatialProxy', action:'layersSearch')}",
                    dataType:"json",
                    data:{
                        q:request.term
                    },
                    success:function (data) {
                        response($.map(data, function (item) {
                            return {
                                label:item.displayname,
                                value:item.displayname,
                                id:item.uid,
                                name:item.name,
                                description:item.description,
                                licence:item.licence_notes,
                                classification1:item.classification1,
                                classification2:item.classification2
                            }
                        }));
                    },
                    error:function (jqXHR, textStatus, errorThrown) {
                        alert("Unable to complete request.\n" + errorThrown);
                    }
                });
            },
            minLength:3,
            html:true,
            select:function (event, ui) {
                var item = ui.item;
                $("#layerInfoPanel").html("");
                $.ajax("${createLink(controller: 'map', action:'layerSummaryFragment')}?layerName=" + item.name + "&callback=addLayerToSet&showInfoButton=true").done(function (content) {
                    $("#layerInfoPanel").html(content);
                });
            }
        });

    });

    function addLayerToSet(layerName) {
        var url = "${createLink(controller:'admin', action:'addLayerToLayerSet', id:layerSet.id)}?layerName=" + encodeURIComponent(layerName);
        window.location = url;
    }

</r:script>
</body>

</html>