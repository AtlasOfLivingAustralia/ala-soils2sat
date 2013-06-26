package au.org.ala.soils2sat

import grails.converters.JSON

class WsController {

    def index() {
        redirect(action:'rifcs')
    }

    def rifcs() {
        def extracts = DataExtraction.findAllByDoiIsNotNull()
        response.contentType = "text/xml"
        def locationMap = [:]
        extracts.each { extract ->
            def locations = []
            extract.studyLocationVisits?.each {
                println it
                def bits = it.split("_")
                if (bits && bits.length > 0) {
                    locations << bits[0]
                }
            }
            locationMap[extract] = locations
        }

        println locationMap

        render(template: 'RIFCS', model:[extracts: extracts, locationMap: locationMap])
    }

}
