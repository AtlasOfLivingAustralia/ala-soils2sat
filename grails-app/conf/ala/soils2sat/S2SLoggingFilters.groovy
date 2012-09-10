package ala.soils2sat

import au.org.ala.soils2sat.LogService
import javax.servlet.http.HttpServletRequest

class S2SLoggingFilters {

    def springSecurityService

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                def userAgent = request.getHeader("user-agent")
                def logService = new LogService()

                def httprequest = (HttpServletRequest) request;
                String requestUri = request.getRequestURI()
                def username = springSecurityService.currentUser?.username ?: "N/A"

                logService.log "Username: ${username} IP: ${httprequest.remoteAddr} Session: ${request.session.id} UA: ${userAgent} URI: ${requestUri}"

            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
