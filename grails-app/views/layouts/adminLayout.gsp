%{--
  - ﻿Copyright (C) 2013 Atlas of Living Australia
  - All Rights Reserved.
  -
  - The contents of this file are subject to the Mozilla Public
  - License Version 1.1 (the "License"); you may not use this file
  - except in compliance with the License. You may obtain a copy of
  - the License at http://www.mozilla.org/MPL/
  -
  - Software distributed under the License is distributed on an "AS
  - IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
  - implied. See the License for the specific language governing
  - rights and limitations under the License.
  --}%

<g:applyLayout name="soils2sat">
    <head>
        <style type="text/css">

        .icon-chevron-right {
            float: right;
            margin-top: 2px;
            margin-right: -6px;
            opacity: .25;
        }

        </style>
    </head>

    <body>

    <r:require module="bootstrap"/>
    <r:require module="panZoom" />

    <div class="container-fluid">
        <legend>
            <table style="width: 100%">
                <tr>
                    <td>Administration<sts:navSeperator/><g:pageProperty name="page.pageTitle"/></td>
                    <td style="text-align: right"><span><g:pageProperty name="page.adminButtonBar"/></span></td>
                </tr>
            </table>
        </legend>

        <div class="row-fluid">
            <div class="span3">
                <ul class="nav nav-list nav-stacked nav-tabs">
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'userList')}" title="Users" />
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'layerSets')}" title="Layer Sets" />
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'layerStyles')}" title="Layer Styles" />
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'searchCriteria')}" title="Search Criteria" />
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'samplingUnits')}" title="Sampling Units" />
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'ecologicalContexts')}" title="Ecological Contexts" />
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'matrix')}" active="Matrix" title="Questions Matrix" />
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'dataExtractions')}" title="Data Extractions" />
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'attachments')}" title="Attachments" />
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'tools')}" title="Tools" />
                    <sts:breadcrumbItem href="${createLink(controller: 'admin', action: 'settings')}" title="Settings" />
                </ul>
            </div>

            <div class="span9">
                <g:if test="${flash.errorMessage}">
                    <div class="container-fluid">
                        <div class="alert alert-error">
                            ${flash.errorMessage}
                        </div>
                    </div>
                </g:if>

                <g:if test="${flash.message}">
                    <div class="container-fluid">
                        <div class="alert alert-info">
                            ${flash.message}
                        </div>
                    </div>
                </g:if>

                <g:layoutBody/>

            </div>
        </div>
    </div>
    </body>
</g:applyLayout>