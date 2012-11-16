package au.org.ala.soils2sat

class QuestionController {

    def springSecurityService

    def index() {
        def questions = Question.list()
        def userInstance = springSecurityService.currentUser as User

        [questions: questions, userInstance: userInstance]
    }

    def ecologicalContexts() {
        def question = Question.get(params.int("questionId"))
        def userInstance = springSecurityService.currentUser as User
        if (question && userInstance) {
            def values = MatrixValue.findAllByQuestionAndRequired(question, true)
            def contexts = values.collect { it.ecologicalContext }
            [question:question, contexts: contexts, userInstance: userInstance]
        }
    }

    def showSamplingUnits() {
        def userInstance = springSecurityService.currentUser as User
        def question = Question.get(params.int("questionId"))
        if (question) {
            def values = MatrixValue.findAllByQuestionAndRequired(question, true)
            def contexts = values.collect { it.ecologicalContext }
            List<EcologicalContext> selectedContexts = []
            contexts.each {
                def key = "context_${it.id}"
                if (params[key] == 'on') {
                    selectedContexts << it
                }
            }

            List<SamplingUnit> units = []
            selectedContexts.each { context ->
                context.samplingUnits.each { unit ->
                    if (!units.contains(unit)) {
                        units << unit
                    }
                }
            }

            [selectedContexts: selectedContexts, question:  question, userInstance: userInstance, samplingUnits: units]
        }

    }
}
