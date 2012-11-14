package au.org.ala.soils2sat

class AdminController {

    def springSecurityService
    def userService

    def index() { }

    def userList() {

        def userList = User.listOrderByUsername()

        userList.removeAll {
            it.username == 'admin'
        }

        [userList: userList]
    }

    def addUser() {
        [:]
    }

    def editUser() {
        def userInstance = User.get(params.int("id"))
        if (userInstance) {
            def model = [userInstance: userInstance]
            params.email = userInstance.username
            params.isAdmin = userService.isAdmin(userInstance)

            render(view: 'addUser', model: model, params:params)
            return
        } else {
            flash.errorMessage = "Could not retrieve user!"
        }

        redirect(action:'userList')
    }

    def saveUser() {

        def errors = []

        User userInstance = null

        if (!params.userId) {
            if (!params.email) {
                errors << "You must supply an email address"
            }
            if (!params.password || !params.password2) {
                errors << "You must supply a password and password confirmation"
            } else {
                if (params.password != params.password2) {
                    errors << "The password confirmation does not match the password"
                }
            }

            def existing = User.findByUsername(params.email)
            if (existing) {
                errors << "This email address has already been registered!"
            }

        } else {
            userInstance = User.get(params.userId)
            if (!userInstance) {
                errors << "Internal error! Unable to find user (or missing user id)"
            }

            if (params.password) {
                if (params.password != params.password2) {
                    errors << "The password confirmation does not match the password"
                }
            }
        }

        if (errors) {

            def sbuf = new StringBuilder("<ul>");

            errors.each {
                sbuf << "<li>" << it << "</li>"
            }

            sbuf << "</ul>"

            flash.errorMessage = sbuf.toString()
            if (params.userId) {
                redirect(controller: 'admin', action: 'editUser', params: params)
            } else {
                redirect(controller: 'admin', action: 'addUser', params: params)
            }
            return
        }


        if (!params.userId) {
            userInstance = new User(username: params.email, password:params.password, enabled: true, accountExpired: false, accountLocked: false)
            userInstance.save(flush: true, failOnError: true)

            def roles = ['ROLE_USER']
            if (params.isAdmin) {
                roles << "ROLE_ADMIN"
            }

            userService.addRoles(userInstance, roles)

        } else {
            if (params.password) {
                userInstance.password = params.password
            }

            if (!params.isAdmin) {
                userService.removeRoles(userInstance, ["ROLE_ADMIN"])
            } else {
                userService.addRoles(userInstance, ["ROLE_ADMIN"])
            }

            userInstance.save(flush: true, failOnError: true)
        }

        redirect(controller: 'admin', action: 'userList')
    }

    def deleteUser = {
        def userToDelete = User.get(params.int("id"))

        if (!userToDelete) {
            flash.errorMessage = "Failed to retrieve user " + params.id
        } else {
            def currentUser = springSecurityService.currentUser as User
            if (currentUser?.id == userToDelete.id) {
                flash.errorMessage = "You cannot delete the account that is currently logged in!"
            } else {
                userService.deleteUser(userToDelete)
                flash.message = "User " + userToDelete.username + " deleted."
            }
        }

        redirect(action: 'userList')
    }

    def layerSets() {
        def layerSets = LayerSet.findAllWhere(user: null).sort() { it.id }

        [layerSets: layerSets]
    }

    def newGlobalLayerSet = {
        def layerSet = new LayerSet(user: null, name:'<new layer set>')
        layerSet.save(flush: true, failOnError: true)
        redirect(controller:'admin', action:'layerSets')
    }

    def deleteLayerSet = {
        def layerSet = LayerSet.get(params.int("id"))
        if (layerSet) {
            layerSet.delete(flush: true)
        }
        redirect(controller:'admin', action:'layerSets')
    }

    def editLayerSet = {
        def layerSet = LayerSet.get(params.int("id"))
        if (!layerSet) {
            flash.errorMessage = "Could not retrieve layer set with id " + params.id
            redirect(controller:'admin', action:'layerSets')
            return
        }

        [layerSet: layerSet]
    }

    def saveLayerSet = {
        def layerSet = LayerSet.get(params.int("id"))
        if (!layerSet) {
            flash.errorMessage = "Could not retrieve layer set with id " + params.id
            redirect(controller:'admin', action:'layerSets')
            return
        }

        layerSet.name = params.name
        layerSet.description = params.description
        layerSet.save(flush:true, failOnError: true)
        redirect(controller:'admin', action:'layerSets')
    }

    def addLayerToLayerSet = {
        def layerSet = LayerSet.get(params.int("id"))
        if (!layerSet) {
            flash.errorMessage = "Could not retrieve layer set with id " + params.id
            redirect(controller:'admin', action:'layerSets')
        } else {
            def layerName = params.layerName
            if (!layerName) {
                flash.errorMessage = "No layer name supplied!"
            } else {
                if (!layerSet.layers.contains(layerName)) {
                    layerSet.addToLayers(layerName)
                    layerSet.save(flush: true, failOnError: true)
                }
            }
            redirect(controller:'admin', action:'editLayerSet', id: layerSet.id)
        }
    }

    def removeLayerFromLayerSet = {
        def layerSet = LayerSet.get(params.int("id"))
        if (!layerSet) {
            flash.errorMessage = "Could not retrieve layer set with id " + params.id
            redirect(controller:'admin', action:'layerSets')
        } else {
            def layerName = params.layerName
            if (!layerName) {
                flash.errorMessage = "No layer name supplied!"
            } else {
                if (layerSet.layers.contains(layerName)) {
                    layerSet.removeFromLayers(layerName)
                    layerSet.save(flush: true, failOnError: true)
                }
            }
            redirect(controller:'admin', action:'editLayerSet', id: layerSet.id)
        }
    }

    def matrix = {
        def contexts = []
        for (EcologicalContextType1 t1 : EcologicalContextType1.values()) {
            for (EcologicalContextType2 t2 : EcologicalContextType2.values()) {
                for (EcologicalContextType3 t3 : EcologicalContextType3.values()) {
                    def context = new EcologicalContext(ecologicalContextType1: t1, ecologicalContextType2: t2, ecologicalContextType3: t3)
                    contexts << context
                }
            }
        }
        def questions = Question.list()
        [contexts: contexts, questions: questions]
    }

    def newQuestion = {
    }

    def insertQuestion = {
        def text = params.question
        def description = params.description

        def question = new Question(text: text, description: description)
        if (!question.validate()) {
            flash.errorMessage = question.errors.toString()
            redirect(controller: 'admin', action:'newQuestion', params:  params)
            return
        }

        question.save(flush: true, failOnError: true)

        redirect(action:'matrix')
    }

    def deleteQuestion = {
        def question = Question.get(params.int("questionId"))
        if (question) {
            question.delete();
        }
        redirect(action:'matrix')
    }

}
