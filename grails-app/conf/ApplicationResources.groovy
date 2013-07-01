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

}
