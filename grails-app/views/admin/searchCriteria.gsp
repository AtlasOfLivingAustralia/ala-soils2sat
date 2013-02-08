<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Search Criteria</title>
    </head>

    <body>

        <script type="text/javascript">

            $(document).ready(function() {

                $("#btnAddCriteria").click(function(e) {
                    e.preventDefault();
                    window.location = "${createLink(controller:'admin', action:'newSearchCriteriaDefinition')}";
                });

                $(".btnEditCriteria").click(function(e) {
                    var criteriaId = $(this).parents("tr[searchCriteriaDefinitionId]").attr("searchCriteriaDefinitionId");
                    if (criteriaId) {
                        window.location = "${createLink(controller: 'admin', action:'editSearchCriteriaDefinition')}?searchCriteriaDefinitionId=" + criteriaId;
                    }
                });

                $(".btnDeleteCriteria").click(function(e) {
                    var criteriaId = $(this).parents("tr[searchCriteriaDefinitionId]").attr("searchCriteriaDefinitionId");
                    if (criteriaId) {
                        if (confirm("Are you sure you want to delete this search criteria definition?")) {
                            window.location = "${createLink(controller: 'admin', action:'deleteSearchCriteriaDefinition')}?searchCriteriaDefinitionId=" + criteriaId;
                        }
                    }
                });


            });

        </script>

        <content tag="pageTitle">Search Criteria</content>
        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Value Type</th>
                    <th>Description</th>
                    <th>Field</th>
                    <th style="width: 120px">Actions</th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${criteriaDefinitions}" var="criteria">
                    <tr searchCriteriaDefinitionId="${criteria.id}">
                        <td>${criteria.name}</td>
                        <td>${criteria.type}</td>
                        <td>${criteria.valueType}</td>
                        <td>${criteria.description}</td>
                        <td>${criteria.fieldName}</td>
                        <td style="">
                            <button class="btn btn-mini btn-danger btnDeleteCriteria"><i class="icon-remove icon-white"></i>&nbsp;delete</button>
                            <button class="btn btn-mini btnEditCriteria"><i class="icon-edit"></i>&nbsp;edit</button>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>
    </body>
    <content tag="adminButtonBar">
      <button class="btn btn-small btn-primary" id="btnAddCriteria"><i class="icon-plus icon-white"></i>&nbsp;Add Criteria Definition</button>
    </content>

</html>