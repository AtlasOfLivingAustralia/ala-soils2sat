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

package au.org.ala.soils2sat

import grails.converters.JSON
import ala.soils2sat.CodeTimer
import org.codehaus.groovy.grails.commons.GrailsApplication

abstract class ServiceBase {

    protected def proxyServiceCall(GrailsApplication grailsApplication, String servicePath, Map params = null) {

        String url = "${serviceRootUrl}/${servicePath}"
        if (params) {
            url = params.serviceUrl ?: url
            params.remove('serviceUrl')
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
        } catch (Exception ex) {
            ex.printStackTrace()
            return null
        } finally {
            timer.stop(true)
        }
    }

    protected abstract String getServiceRootUrl()

}
