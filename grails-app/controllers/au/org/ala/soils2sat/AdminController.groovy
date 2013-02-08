package au.org.ala.soils2sat

import grails.converters.JSON

class AdminController {

    def springSecurityService
    def userService
    def layerService

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

    def deleteUser() {
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

    def newGlobalLayerSet() {
        def layerSet = new LayerSet(user: null, name:'<new layer set>')
        layerSet.save(flush: true, failOnError: true)
        redirect(controller:'admin', action:'layerSets')
    }

    def deleteLayerSet() {
        def layerSet = LayerSet.get(params.int("id"))
        if (layerSet) {
            layerSet.delete(flush: true)
        }
        redirect(controller:'admin', action:'layerSets')
    }

    def editLayerSet() {
        def layerSet = LayerSet.get(params.int("id"))
        if (!layerSet) {
            flash.errorMessage = "Could not retrieve layer set with id " + params.id
            redirect(controller:'admin', action:'layerSets')
            return
        }

        [layerSet: layerSet]
    }

    def saveLayerSet() {
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

    def addLayerToLayerSet() {
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

    def removeLayerFromLayerSet() {
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

    def matrix() {
        def contexts = EcologicalContext.listOrderByName()
        def questions = Question.list().sort { it.id }
        def valuesList = MatrixValue.list();
        def valueMap =[:]
        valuesList.each {
            def key = "${it.ecologicalContext.id}_${it.question.id}"
            valueMap[key] = it
        }

        [contexts: contexts, questions: questions, valueMap: valueMap]
    }

    def newQuestion() {
    }

    def editQuestion() {
        def question = Question.get(params.int("questionId"))
        if (question) {
            [question: question]
        } else {
            flash.message = "Could not retrieve question!"
            redirect(action:'matrix')
            return
        }

    }

    def insertQuestion() {
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

    def updateQuestion() {

        def question = Question.get(params.int("questionId"))
        if (question) {
            question.text = params.question
            question.description = params.description
            question.save(flush: true, failOnError: true)
        }

        redirect(action:'matrix')
    }

    def deleteQuestion() {
        def question = Question.get(params.int("questionId"))
        if (question) {
            question.delete();
        }
        redirect(action:'matrix')
    }

    def ecologicalContexts() {
        def contexts = EcologicalContext.listOrderByName();
        [contexts: contexts]
    }

    def generateEcologicalContexts() {
        for (EcologicalContextType1 t1 : EcologicalContextType1.values()) {
            for (EcologicalContextType2 t2 : EcologicalContextType2.values()) {
                for (EcologicalContextType3 t3 : EcologicalContextType3.values()) {
                    def name = "${t1.description} + ${t2.description} + ${t3.description}"
                    def existing = EcologicalContext.findByName(name)
                    if (!existing) {
                        def context = new EcologicalContext(name: name)
                        context.save(flush: true, failOnError: true)
                    }
                }
            }
        }

        redirect(action:'ecologicalContexts')
    }

    def deleteEcologicalContext() {
        def context = EcologicalContext.get(params.int("ecologicalContextId"))
        if (context) {
            context.delete(flush: true)
        }

        redirect(action: 'ecologicalContexts')
    }

    def newEcologicalContext() {

    }

    def insertEcologicalContext() {
        def context = new EcologicalContext(params)

        if (!context.validate()) {
            flash.errorMessage = context.errors.toString()
            redirect(action:'newEcologicalContext', params: params)
            return
        }

        context.save(flush: true, failOnError: true)

        redirect(action:'ecologicalContexts')
    }

    def editEcologicalContext() {
        def context = EcologicalContext.get(params.int("ecologicalContextId"))
        if (!context) {
            flash.errorMessage = "Could not location specified context!"
            redirect(action:'ecologicalContexts')
            return
        }
        [context: context, samplingUnits: SamplingUnit.listOrderByName()]
    }


    def updateEcologicalContext() {
        def context = EcologicalContext.get(params.int("ecologicalContextId"))
        if (context) {
            context.properties = params
            if (!context.validate()) {
                flash.errorMessage = context.errors.toString()
                redirect(action:'editEcologicalContext', params: params)
            }

            context.save(flush: true, failOnError: true)
        } else {
            flash.errorMessage = "Could not locate specified context!"
        }
        redirect(action:'ecologicalContexts')
    }
    
    def setMatrixValue() {
        def question = Question.get(params.int("questionId"));
        def context = EcologicalContext.get(params.int("ecologicalContextId"));
        String value = params.value as String

        if (question && context) {
            
            def existing = MatrixValue.findByEcologicalContextAndQuestion(context, question)
            if (existing && !value) {
                existing.delete(flush: true)
            } else {

                if (!existing) {
                    existing = new MatrixValue(question: question, ecologicalContext: context)
                }

                if (value?.equalsIgnoreCase("y")) {
                    existing.required = true
                } else if (value?.equalsIgnoreCase('n')) {
                    existing.required = false
                } else {
                    existing.required = null;
                }

                existing.save(flush: true, failOnError: true)
            }
        }
        render ([status:'ok'] as JSON)
    }

    def contextSamplingUnitsFragment() {
        def context = EcologicalContext.get(params.int("ecologicalContextId"))
        render(view:'contextSamplingUnitsFragment', model: [context: context])
    }

    def addContextSamplingUnitAjax() {
        def context = EcologicalContext.get(params.int("ecologicalContextId"))
        def unit = SamplingUnit.get(params.int("samplingUnitId"))
        if (context && unit) {
            if (!context.samplingUnits.contains(unit)) {
                context.addToSamplingUnits(unit)
                context.save(flush: true, failOnError: true)
            }
        }
        render ([status:'ok'] as JSON)
    }

    def removeContextSamplingUnitAjax() {
        def context = EcologicalContext.get(params.int("ecologicalContextId"))
        def unit = SamplingUnit.get(params.int("samplingUnitId"))
        if (context && unit) {
            if (context.samplingUnits.contains(unit)) {
                context.removeFromSamplingUnits(unit)
            }
        }
        render ([status:'ok'] as JSON)

    }

    def samplingUnits() {
        [samplingUnits: SamplingUnit.listOrderByName()]
    }

    def addSamplingUnit() {
        def name = params.name?.trim()

        if (name) {
            def existing = SamplingUnit.findByName(name)
            if (!existing) {
                def samplingUnit = new SamplingUnit(name: name)
                samplingUnit.save(flush: true, failOnError: true)
            }
        }

        redirect(controller: 'admin', action:'samplingUnits')
    }

    def deleteSamplingUnit() {
        def unit = SamplingUnit.get(params.int("samplingUnitId"))
        if (unit) {
            try {
                unit.delete(flush: true)
            } catch (Exception ex) {
                flash.errorMessage = "Cannot delete sampling unit - it is probably being used by one or more ecological contexts"
            }
        }
        redirect(controller: 'admin', action:'samplingUnits')
    }

    def advancedSettings() {

    }

    def clearLayersCacheAjax() {
        layerService.flushCache()
    }

    def searchCriteria() {
        def criteriaDefinitions = SearchCriteriaDefinition.findAll()

        [criteriaDefinitions: criteriaDefinitions]
    }

    def newSearchCriteriaDefinition() {
        SearchCriteriaDefinition criteriaDefinition = null
        render(view: 'editSearchCriteriaDefinition', model: [criteriaDefinition:  criteriaDefinition])
    }

    def editSearchCriteriaDefinition() {
        SearchCriteriaDefinition criteriaDefinition = SearchCriteriaDefinition.get(params.int("searchCriteriaDefinitionId"))
        render(view: 'editSearchCriteriaDefinition', model: [criteriaDefinition:  criteriaDefinition])
    }

    def saveSearchCriteriaDefinition() {
        def criteria = SearchCriteriaDefinition.get(params.int("searchCriteriaDefinitionId"))
        if (criteria == null) {
            criteria = new SearchCriteriaDefinition(params)
        } else {
            criteria.properties = params
        }

        criteria.save()

        redirect(action:"searchCriteria")
    }

    def deleteSearchCriteriaDefinition() {
        def criteria = SearchCriteriaDefinition.get(params.int("searchCriteriaDefinitionId"))
        if (criteria) {
            criteria.delete(flush: true)
        }

        redirect(action:"searchCriteria")
    }

    def createDefaultSearchCriteria() {
        def criteriaDefinitions = [
                [name: 'IBRA Region', description: "IBRA Region name", type: CriteriaType.SpatialPortalField, valueType: CriteriaValueType.StringMultiSelect, fieldName: 'cl20'],
                [name: 'Jurisdiction', description: "Australian State name", type: CriteriaType.SpatialPortalField, valueType: CriteriaValueType.StringMultiSelect, fieldName: 'cl22'],
                [name: 'IBRA Sub Region', description: "IBRA Sub Region name",type: CriteriaType.SpatialPortalField, valueType: CriteriaValueType.StringMultiSelect, fieldName: 'cl914'],
                [name: 'NRM Region', description: "National Resource Management Region name", type: CriteriaType.SpatialPortalField, valueType: CriteriaValueType.StringMultiSelect, fieldName: 'cl916'],
                [name: 'LGA Region', description: "Local Government Region name", type: CriteriaType.SpatialPortalField, valueType: CriteriaValueType.StringMultiSelect, fieldName: 'cl959'],
                [name: 'Geology Type', description: "Surface Geology", type: CriteriaType.SpatialPortalLayer, valueType: CriteriaValueType.StringDirectEntry, fieldName: 'lith_geologicalunitpolygons1m'],
                [name: 'Annual Rainfall', description: "Mean annual rainfall", type: CriteriaType.SpatialPortalLayer, valueType: CriteriaValueType.NumberRangeDouble, fieldName: 'rainm'],
                [name: 'Distance from coast', description: "Distance from nearest coastline", type: CriteriaType.SpatialPortalLayer, valueType: CriteriaValueType.NumberRangeDouble, fieldName: 'substrate_distcoast'],
                [name: 'Distance from water', description: "Distance from nearest water", type: CriteriaType.SpatialPortalLayer, valueType: CriteriaValueType.NumberRangeDouble, fieldName: 'substrate_distanywater']
        ]

        criteriaDefinitions.each { criteriaDefinition ->
            def existing = SearchCriteriaDefinition.findByName(criteriaDefinition.name)
            if (!existing) {
                def newDefinition = new SearchCriteriaDefinition(criteriaDefinition)
                newDefinition.save()
            }
        }


        redirect(action:'advancedSettings')
    }

}
