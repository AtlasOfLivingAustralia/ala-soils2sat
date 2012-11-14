package au.org.ala.soils2sat

class QuestionController {

    def springSecurityService

    def index() {
        def questions = Question.list()
        def userInstance = springSecurityService.currentUser as User

        [questions: questions, userInstance: userInstance]
    }
}
