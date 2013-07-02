/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH

modules = {

    application {
        resource url:'js/application.js'
    }

    mouseWheel {
        dependsOn 'jquery'
        resource url:'js/jquery.mousewheel.min.js'
    }

    panZoom {
        dependsOn 'jquery,mouseWheel'
        resource url:'js/jquery-panZoom.js'
    }

    openlayers {
        defaultBundle false
        resource url: 'http://dev.openlayers.org/releases/OpenLayers-2.11/OpenLayers.js'
        resource url: 'http://dev.openlayers.org/releases/OpenLayers-2.11/theme/default/style.css'
    }

    visualisationHandlers {
        dependsOn 'jquery'
        resource url:'/js/visualisationEventHandlers.js'
    }

    overrides {
        'jquery-theme' {
            resource id:'theme', url:'/css/smoothness/jquery-ui-1.8.23.custom.css'
        }
    }

}
