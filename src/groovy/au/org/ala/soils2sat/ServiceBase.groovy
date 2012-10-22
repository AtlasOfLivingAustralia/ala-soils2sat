package au.org.ala.soils2sat

import grails.converters.JSON
import ala.soils2sat.CodeTimer
import org.codehaus.groovy.grails.commons.GrailsApplication

/**
 * Created with IntelliJ IDEA.
 * User: baird
 * Date: 19/10/12
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
abstract class ServiceBase {

    private Map<String, String> _serviceCache = [:]

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

        if (_serviceCache.containsKey(url)) {
            return JSON.parse(_serviceCache[url])
        }

        def timer = new CodeTimer("Service call: ${url}")

        try {
            def u = new URL(url)
            def results = u.getText()
            _serviceCache.put(url, results)
            return JSON.parse(results)
        } finally {
            timer.stop(true)
        }
    }

    protected abstract String getServiceRootUrl()


}
