package au.org.ala.soils2sat

class WsController {

    def studyLocationService

    def index() {
        redirect(action:'rifcs')
    }

    def rifcs() {
        def extracts = DataExtraction.findAllByDoiIsNotNull()
        response.contentType = "text/xml"
        def locationMap = [:]
        extracts.each { extract ->
            def locations = [:]
            extract.studyLocationVisits?.each {
                def bits = it.split("_")
                if (bits && bits.length > 0) {
                    def studyLocationName = bits[0]
                    def studyLocationDetails = studyLocationService.getStudyLocationDetails(studyLocationName)
                    locations[studyLocationName] = [latitude:studyLocationDetails.latitude, longitude: studyLocationDetails.longitude]
                }
            }
            locationMap[extract] = locations
        }

        render(template: 'RIFCS', model:[extracts: extracts, locationMap: locationMap])
    }

}
