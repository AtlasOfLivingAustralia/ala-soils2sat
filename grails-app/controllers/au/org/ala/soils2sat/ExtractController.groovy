package au.org.ala.soils2sat

class ExtractController {

    def springSecurityService

    def index() {
        def user = springSecurityService.currentUser as User
        if (user) {
            def appState = user.applicationState
            redirect(action:'extractStudyLocationVisitData')
            return
        }
        flash.message = "No user!"
        redirect(controller: 'map', action:'index')
    }

    def extractStudyLocationData() {

    }

    def extractStudyLocationVisitData() {

    }

}
