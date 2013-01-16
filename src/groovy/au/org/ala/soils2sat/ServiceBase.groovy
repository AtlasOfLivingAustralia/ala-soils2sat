package au.org.ala.soils2sat

import grails.converters.JSON
import ala.soils2sat.CodeTimer
import org.codehaus.groovy.grails.commons.GrailsApplication

abstract class ServiceBase {

    protected def proxyServiceCall(GrailsApplication grailsApplication, String servicePath, Map params = null) {

        String url = "${serviceRootUrl}/${servicePath}"
        if (params) {
            url += '?'
            params.each {
                url += it.key + "=" + it.value + '&'
            }
            if (url.endsWith('&')) {
                url = url.subSequence(0, url.length() - 1);
            }
        }

        def timer = new CodeTimer("Service call: ${url}")

        try {
            def u = new URL(url)
            def results = u.getText()
            return JSON.parse(results)
        } finally {
            timer.stop(true)
        }
    }

    protected abstract String getServiceRootUrl()

}
