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

import javax.servlet.Filter
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import java.util.regex.Pattern
import java.util.regex.Matcher
import au.org.ala.soils2sat.LogService
import org.springframework.web.context.support.WebApplicationContextUtils

class S2SServletFilter implements Filter  {

    private List<Pattern> _filterPatterns;

    void init(FilterConfig filterConfig) {
        _filterPatterns = new ArrayList<Pattern>();
        addPattern(".*/plugins/.*")
        addPattern(".*/js/.*")
        addPattern(".*/css/.*")
        addPattern(".*/images/.*")
    }

    private void addPattern(String pattern) {
        _filterPatterns.add(Pattern.compile(pattern))
    }

    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
//            def request = servletRequest as HttpServletRequest
//            if (request) {
//                request.getSession().getServletContext()
//                boolean doLog = true;
//                String requestUri = request.getRequestURI()
//                for (Pattern p : _filterPatterns) {
//                    Matcher m = p.matcher(requestUri)
//                    if (m.find()) {
//                        doLog = false;
//                        break;
//                    }
//                }
//
//                if (doLog) {
//                    def userAgent = request.getHeader("user-agent")
//                    def logService = new LogService()
//
//                    def httprequest = (HttpServletRequest) request;
//
//                    logService.log "IP: ${httprequest.remoteAddr} Session: ${request.session.id} UA: ${userAgent} URI: ${requestUri}"
//                }
//
//            }
        } finally {
            filterChain.doFilter(servletRequest, servletResponse)
        }
    }

    void destroy() {
    }
}
