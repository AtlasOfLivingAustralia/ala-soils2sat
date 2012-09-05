import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH

modules = {
    application {
        resource url:'js/application.js'
    }
    jquery {
        resource url:'js/jquery-1.8.0.min.js'
    }
    jqueryui {
        dependsOn 'jquery'
        resource url: 'js/jquery-ui-1.8.23.custom.min.js'
        resource url: 'css/smoothness/jquery-ui-1.8.23.custom.css'
    }

    openlayers {
        defaultBundle false
        resource url: 'http://dev.openlayers.org/releases/OpenLayers-2.11/OpenLayers.js'
        resource url: 'http://dev.openlayers.org/releases/OpenLayers-2.11/theme/default/style.css'
    }

    fancybox {
        resource url:'/fancybox/jquery.fancybox.css'
        resource url:'/fancybox/jquery.fancybox.pack.js'
        resource url:'/fancybox/blank.gif'
        resource url:'/fancybox/fancybox_loading.gif'
        resource url:'/fancybox/fancybox_overlay.png'
        resource url:'/fancybox/fancybox_sprite.png'
    }

    bootstrap {
        resource url: '/bootstrap/js/bootstrap.min.js'
        resource url: '/bootstrap/css/bootstrap.min.css'
        resource url: '/bootstrap/img/glyphicons-halflings-white.png'
        resource url: '/bootstrap/img/glyphicons-halflings.png'
    }

    bootstrap_responsive {
        dependsOn 'bootstrap'
        resource url: '/bootstrap/css/bootstrap-responsive.min.css'
    }

}

def getFilesForPath(path) {

    def webFileCachePaths = []

    def servletContext = SCH.getServletContext()

    //context isn't present when testing in integration mode. -jg
    if(!servletContext) return webFileCachePaths

    def realPath = servletContext.getRealPath('/')

    def appDir = new File("$realPath/$path")

    appDir.eachFileRecurse {File file ->
        if (file.isDirectory()) return
        webFileCachePaths << file.path.replace(realPath, '')
    }

    return webFileCachePaths
}
