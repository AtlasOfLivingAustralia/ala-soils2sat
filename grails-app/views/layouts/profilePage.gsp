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
    <r:require module="bootstrap-responsive-css"/>
    <div class="container-fluid">
        <legend>
            <table style="width: 100%">
                <tr>
                    <td>
                        <sts:homeBreadCrumb />
                        <sts:navSeperator/>
                        User Profile
                        <sts:navSeperator/>
                        <span class="sts-breadcrumb"><g:pageProperty name="page.pageTitle"/></span>
                    </td>
                    <td style="text-align: right"><span><g:pageProperty name="page.profileButtonBar"/></span></td>
                </tr>
            </table>
        </legend>

        <div class="row-fluid">
            <div class="span3">
                <ul class="nav nav-list nav-stacked nav-tabs">
                    <sts:breadcrumbItem href="${createLink(controller: 'userProfile', action: 'index')}" title="Details" />
                    <sts:breadcrumbItem href="${createLink(controller: 'userProfile', action: 'extractions')}" title="My Extractions" active="Extractions" />
                    <sts:breadcrumbItem href="${createLink(controller: 'userProfile', action: 'listLayerSets')}" title="My Layer Sets" active="Layer Sets" />
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