package au.org.ala.soils2sat

import grails.converters.JSON
import javax.servlet.http.HttpServletResponse
import ala.soils2sat.CodeTimer

class AjaxController {

    def plotService

    def getPlots() {

        def results = plotService.getPlots()
        render (results as JSON)
        // proxyServiceCall("getPlots", response)
    }

    def getPlotSummary() {
        def plotName = params.plotName
        if (plotName) {

        }

    }

    private def proxyServiceCall(String servicePath, HttpServletResponse response) {
        BufferedReader reader = null;
        String url = "${grailsApplication.config.aekosServiceRoot}/${servicePath}"
        def buffer = new StringBuilder()

        def timer = new CodeTimer("Service call: ${url}")

        try {
            def u = new URL(url)
            reader = u.newReader()
            String line = null
            while (line = reader.readLine()) {
                buffer.append(line);
            }
        } catch (Exception ex) {
            buffer.append([error: ex.toString()] as JSON)
        } finally {
            if (reader) {
                reader.close();
            }
            timer.stop(true)
        }
        response.contentType = "text/json"
        render(buffer)
    }

}
