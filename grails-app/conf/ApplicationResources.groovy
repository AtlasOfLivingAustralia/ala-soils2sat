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
        resource url: 'js/openlayers/OpenLayers.js'
        def allowedExtensions = ['png']
        getFilesForPath('/js/openlayers/img').each {
            def path = it as String
            if (!path.contains(".svn")) {
                def extension = path.substring(path.lastIndexOf(".") + 1)?.toLowerCase()
                if (allowedExtensions.contains(extension)) {
                    println "Adding resource: " + it
                    resource url: it
                }
            }
        }
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
