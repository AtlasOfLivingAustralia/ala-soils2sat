<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!doctype html>
<html>
    <head>
        <r:require module='jqueryui'/>
        <r:require module='bootstrap_responsive'/>
        <meta name="layout" content="adminLayout"/>
        <title>Soils to Satellites - Admin - Attachments</title>
    </head>

    <body>

        <script type="text/javascript">

            $(document).ready(function() {

                $(".btnDelete").click(function(e) {
                    e.preventDefault();
                    var attachmentId = $(this).parents("[attachmentId]").attr("attachmentId");
                    if (attachmentId) {
                        window.location = "${createLink(controller:'attachment', action:'delete')}?id=" + attachmentId;
                    }
                });

                $(".btnEdit").click(function(e) {
                    e.preventDefault();
                    var attachmentId = $(this).parents("[attachmentId]").attr("attachmentId");
                    if (attachmentId) {
                        window.location = "${createLink(controller:'attachment', action:'edit')}?id=" + attachmentId;
                    }
                });


                $(".btnPreview").click(function(e) {
                    var attachmentId = $(this).parents("[attachmentId]").attr("attachmentId");
                    if (attachmentId) {
                        showModal({
                            url: "${createLink(controller: 'attachment', action:'imagePreviewFragment')}/" + attachmentId,
                            title: "Image Preview",
                            height: 500,
                            width: 800
                        });
                    }

                });

            });

        </script>

        <content tag="pageTitle">Attachments</content>

        <content tag="adminButtonBar">
            <a href="${createLink(controller:'attachment', action:'upload')}" class="btn btn-small btn-primary" id="btnUpload"><i class="icon-upload icon-white"></i>&nbsp;Upload attachment</a>
        </content>

        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <g:sortableColumn title="Date Uploaded" property="dateUploaded" />
                    <g:sortableColumn title="OwnerId" property="ownerId" />
                    <g:sortableColumn title="Category" property="category" />
                    <g:sortableColumn title="Name" property="name" />
                    <g:sortableColumn title="Mime type" property="mimeType" />
                    <g:sortableColumn title="Size" property="size" />
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${attachments}" var="attachment">
                    <tr attachmentId="${attachment.id}">
                        <td><g:formatDate date="${attachment.dateUploaded}" format="${au.org.ala.soils2sat.DateUtils.S2S_DATE_TIME_FORMAT}" /> </td>
                        <td>${attachment.studyLocationName}<g:if test="${attachment.studyLocationVisitStartDate}">_${attachment.studyLocationVisitStartDate}</g:if></td>
                        <td>${attachment.category}</td>
                        <td>${attachment.name}</td>
                        <td>${attachment.mimeType}</td>
                        <td>${attachment.size}</td>
                        <td style="min-width:130px">
                            <button class="btn btn-mini btn-danger btnDelete" title="Delete attachment"><i class="icon-remove icon-white"></i></button>
                            <button class="btn btn-mini btnEdit" title="Edit attachment"><i class="icon-edit"></i></button>
                            <a class="btn btn-mini" href="${createLink(controller: 'attachment', action:'download', id: attachment.id)}" title="Download attachment">
                                <i class="icon-download-alt"></i>
                            </a>

                            <g:if test="${attachment.mimeType.startsWith("image/")}">
                                <button class="btn btn-mini btnPreview" title="Preview"><i class="icon-picture"></i></button>
                            </g:if>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>
        <div class="pagination">
            <g:paginate total="${attachments.totalCount}" omitNext="true" omitPrev="true" />
        </div>
    </body>

</html>