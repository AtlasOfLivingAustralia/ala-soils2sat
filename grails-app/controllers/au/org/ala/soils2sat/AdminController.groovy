package au.org.ala.soils2sat

import grails.converters.JSON
import org.grails.plugins.csv.CSVWriter
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

class AdminController {

    def springSecurityService
    def userService
    def layerService
    def studyLocationService

    def index() { }

    def userList() {

        def userList = User.list()

        userList.removeAll {
            it.username == 'admin'
        }

        [userList: userList.sort({ it.applicationState?.lastLogin }).reverse()]
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

        def layerNames = [:]
        layerSet.layers?.each { layerName ->
            def layerInfo = layerService.getLayerInfo(layerName)
            layerNames[layerName] = layerInfo.displayname
        }


        [layerSet: layerSet, displayNames: layerNames]
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
            flash.errorMessage = "Could not retrieve question!"
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
            try {
                question.delete(flush: true);
            } catch (Exception ex) {
                flash.errorMessage = "Delete failed: " + ex.message
            }
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
            try {
                context.delete(flush: true)
            } catch (Exception ex) {
                flash.errorMessage = "Delete failed! " + ex.message
            }
        }

        redirect(action: 'ecologicalContexts')
    }

    def deleteAllEcologicalContexts() {
        try {
            EcologicalContext.deleteAll(EcologicalContext.list())
        } catch (Exception ex) {
            flash.errorMessage = "Delete All failed: " + ex.message
        }

        redirect(action:'ecologicalContexts')
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

    def removeAllContextSamplingUnits() {
        def context = EcologicalContext.get(params.int("ecologicalContextId"))
        if (context) {
            try {
                context.samplingUnits.clear();
            } catch (Exception ex) {
                flash.errorMessage = "Failed to remove all sampling units: " + ex.message
            }
        }

        redirect(action: 'editEcologicalContext', params: [ecologicalContextId: context?.id])
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

    def deleteAllSamplingUnits() {
        try {
            SamplingUnit.deleteAll(SamplingUnit.list())
        } catch (Exception ex) {
            flash.errorMessage = "Delete All failed: " + ex.message
        }

        redirect(action: "samplingUnits")
    }

    def tools() {

    }

    def settings() {
        def settings = Setting.list()
        [settings: settings]
    }

    def editSetting() {
        def setting = Setting.get(params.id)
        [setting: setting]
    }

    def updateSetting() {
        def setting = Setting.get(params.id)
        if (setting) {
            setting.value = params.value
            flash.message ="Setting ${setting?.key} updated"
        } else {
            flash.message = "Setting not saved!!! Empty or invalid setting id specified"
        }

        redirect(action:'settings')
    }

    def clearLayersCacheAjax() {
        layerService.flushCache()
    }

    def clearAusPlotsCacheAjax() {
        studyLocationService.flushCache()
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
            try {
                criteria.delete(flush: true)
            } catch (Exception ex) {
                flash.errorMessage = "Delete failed. This is probably because there exists search critera that use this definition<br/>" + ex.message
            }
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


        redirect(action:'tools')
    }

    def exportSamplingUnits() {

        def units = SamplingUnit.list()
        def columns = ['SamplingUnit']

        response.setHeader("Content-disposition", "attachment;filename=SamplingUnits.csv")
        response.contentType = "text/csv"

        def bos = new OutputStreamWriter(response.outputStream)

        def writer = new CSVWriter(bos, {
            SamplingUnitName { it.name }
        })

        units.each {
            writer << it
        }

        bos.flush()
        bos.close()
    }

    def selectImportFile() {
        def importType = params.importType as String;
        def heading = ''
        def importAction = ''
        def cancelUrl = ''
        switch(importType) {
            case "samplingUnits":
                heading = "Import Sampling Units"
                importAction = 'importSamplingUnits'
                cancelUrl = createLink(action: 'samplingUnits')
                break
            case "matrix":
                heading = "Import matrix from JSON file"
                importAction = "importMatrix"
                cancelUrl = createLink(action: 'matrix')
                break
            case "layerSets":
                heading = "Import Layer Sets from JSON file"
                importAction = "importLayerSets"
                cancelUrl = createLink(action: 'layerSets')
                break
            case "searchCriteriaDefinitions":
                heading = "Import Search Criteria Definitions from JSON file"
                importAction = "importSearchCriteriaDefinitions"
                cancelUrl = createLink(action: "searchCriteria")
                break
        }

        if (!heading) {
            redirect(action: 'index')
            return
        }

        [heading: heading, importType: importType, importAction: importAction, cancelUrl: cancelUrl]
    }

    def importSamplingUnits() {

        if(request instanceof MultipartHttpServletRequest) {
            MultipartFile f = ((MultipartHttpServletRequest) request).getFile('filename')
            if (f != null) {
                def allowedMimeTypes = ['text/csv']
                if (!allowedMimeTypes.contains(f.getContentType())) {
                    flash.errorMessage = "The file must be one of: ${allowedMimeTypes}"
                    redirect(action:'samplingUnits')
                    return;
                }
                int lineCount = 0
                int importCount = 0
                f.inputStream.toCsvReader([skipLines: 1]).eachLine { tokens ->
                    lineCount++
                    def newName = tokens[0]
                    def existing = SamplingUnit.findByName(newName)
                    if (!existing) {
                        def samplingUnit = new SamplingUnit(name: newName)
                        samplingUnit.save()
                        importCount++
                    }
                    println tokens
                }
                flash.message ="${lineCount} line(s) processed, ${importCount} new sampling units created."
            } else {
                flash.errorMessage ="No file selected!"
            }
        }

        redirect(action: 'samplingUnits')
    }

    def exportSearchCriteriaDefinitions() {
        def data = []

        def criteriaDefinitions = SearchCriteriaDefinition.list()
        criteriaDefinitions.each {
            data << [name: it.name, description: it.description, fieldName: it.fieldName, type: it.type.toString(), valueType: it.valueType.toString()]
        }

        writeJSON(data, 'searchCriteria')
    }

    def importSearchCriteriaDefinitions() {
        if(request instanceof MultipartHttpServletRequest) {
            MultipartFile f = ((MultipartHttpServletRequest) request).getFile('filename')
            if (f != null) {
                def allowedMimeTypes = ['application/json', 'application/octet-stream']
                if (!allowedMimeTypes.contains(f.getContentType())) {
                    flash.errorMessage = "The file must be one of: ${allowedMimeTypes}"
                    redirect(action:'searchCriteria')
                    return;
                }

                def data = JSON.parse(f.inputStream, "utf-8")
                if (data) {
                    data.each {
                        def existing = SearchCriteriaDefinition.findByName(it.name)
                        if (!existing) {
                            existing = new SearchCriteriaDefinition(it)
                            existing.save(flush: true, failOnError: true)
                        }
                    }
                }
            }
        }
        redirect(action: 'searchCriteria')
    }

    private writeJSON(Object data, String filename) {

        response.setHeader("Content-disposition", "attachment;filename=${filename}.json")
        response.contentType = "text/csv"

        def writer = new OutputStreamWriter(response.outputStream)

        try {
            writer.write((data as JSON).toString(true))
        } finally {
            if (writer) {
                writer.flush()
                writer.close()
            }
        }

    }


    def exportLayerSets() {
        def layerSets = LayerSet.findAllWhere(user: null)
        def data = []

        layerSets.each { layerSet ->
            def set = [name:layerSet.name, description: layerSet.description, layerSet: []]
            layerSet.layers?.each { layerName ->
                set.layerSet << layerName
            }
            data << set
        }
        writeJSON(data, "layerSets")
    }

    def importLayerSets() {
        if(request instanceof MultipartHttpServletRequest) {
            MultipartFile f = ((MultipartHttpServletRequest) request).getFile('filename')
            if (f != null) {
                def allowedMimeTypes = ['application/json']
                if (!allowedMimeTypes.contains(f.getContentType())) {
                    flash.errorMessage = "The file must be one of: ${allowedMimeTypes}"
                    redirect(action:'layerSets')
                    return;
                }

                def data = JSON.parse(f.inputStream, "utf-8")
                if (data) {
                    data.each {
                        def existing = LayerSet.findByNameAndUser(it.name, null)
                        if (!existing) {
                            existing = new LayerSet(user: null, name: it.name, description: it.description)
                            existing.save(flush: true, failOnError: true)
                        }
                        it.layerSet?.each { layerName ->
                            if (!existing.layers?.contains(layerName)) {
                                existing.addToLayers(layerName)
                            }
                        }
                    }
                }
            }
        }
        redirect(action: 'layerSets')
    }

    def exportMatrix() {

        def data = [:]
        // First do samplingUnits
        def samplingUnits = SamplingUnit.list();
        data.samplingUnits = []
        samplingUnits.each {
            data.samplingUnits << [name: it.name, id:  it.id]
        }
        // then ecological contexts
        def ecologicalContexts = EcologicalContext.list()
        data.ecologicalContexts = []
        ecologicalContexts.each {
            data.ecologicalContexts << [name: it.name, description: it.description, samplingUnits: it.samplingUnits.collect { it.id }, id:it.id]
        }
        // then the questions
        def questions = Question.list()
        data.questions = []
        questions.each {
            data.questions << [text: it.text, description: it.description, id: it.id]
        }
        // finally the matrix values...
        def matrixValues = MatrixValue.findAll()
        data.matrixValues = [:]
        matrixValues.each {
            if (it.required != null) {
                def key = "${it.question.id}_${it.ecologicalContext.id}"
                data.matrixValues[key] = it.required
            }
        }

        writeJSON(data, "matrix")
    }

    def importMatrix() {
        if(request instanceof MultipartHttpServletRequest) {
            MultipartFile f = ((MultipartHttpServletRequest) request).getFile('filename')
            if (f != null) {
                def allowedMimeTypes = ['application/json']
                if (!allowedMimeTypes.contains(f.getContentType())) {
                    flash.errorMessage = "The file must be one of: ${allowedMimeTypes}"
                    redirect(action:'matrix')
                    return;
                }

                def data = JSON.parse(f.inputStream, "utf-8")

                def results = [samplingUnitsAdded: 0, ecologicalContextsAdded: 0, contextSamplingUnitsBound: 0, questionsAdded: 0, matrixValuesAdded: 0 ]
                Map<Long, Long> samplingUnitMap = [:]
                Map<Long, Long> ecologicalContextMap = [:]
                Map<Long, Long> questionMap = [:]

                // first ensure all the sampling units are in
                data.samplingUnits.each {
                    def existing = SamplingUnit.findByName(it.name)
                    if (!existing) {
                        existing = new SamplingUnit(name: it.name)
                        existing.save(flush: true, failOnError: true)
                        results.samplingUnitsAdded++
                    }
                    samplingUnitMap[it.id as Long] = existing.id
                }

                // Then ecological contexts
                data.ecologicalContexts.each { context ->
                    def existing = EcologicalContext.findByName(context.name)
                    if (!existing) {
                        existing = new EcologicalContext(name: context.name, description: context.description)
                        existing.save(flush: true, failOnError: true)
                        results.ecologicalContextsAdded++
                    }
                    ecologicalContextMap[context.id as Long] = existing.id
                    // Now the sampling units attached to the ecological context
                    context.samplingUnits.each { unitId ->
                        def mappedId = samplingUnitMap[unitId as Long] as Long
                        def samplingUnit = SamplingUnit.get(mappedId)
                        if (!samplingUnit) {
                            throw new RuntimeException("Internal error! Could not locate sampling unit via mapped id!")
                        }
                        if (!existing.samplingUnits?.contains(samplingUnit)) {
                            existing.addToSamplingUnits(samplingUnit)
                            results.contextSamplingUnitsBound++
                        }
                    }
                }

                // Now questions
                data.questions.each { question ->
                    def existing = Question.findByText(question.text)
                    if (!existing) {
                        existing = new Question(text: question.text, description: question.description)
                        existing.save(flush: true, failOnError: true)
                        results.questionsAdded++
                    }
                    questionMap[question.id as Long] = existing.id
                }

                // And finally the matrix values...
                data.matrixValues.each { kvp ->
                    def bits = kvp.key.split("_")
                    def questionId = questionMap[Long.parseLong(bits[0])]
                    def contextId = ecologicalContextMap[Long.parseLong(bits[1])]

                    println "questionID: ${questionId} contextID: ${contextId}"

                    def question = Question.get(questionId)
                    if (!question) {
                        throw new RuntimeException("Failed to retrieve a value Matrix Value Question from mapped ids! ID " + questionId)
                    }

                    def ecologicalContext = EcologicalContext.get(contextId)
                    if (!ecologicalContext) {
                        throw new RuntimeException("Failed to retrieve a value Matrix Value Question from mapped ids! ID " + contextId)
                    }

                    def existing = MatrixValue.findByQuestionAndEcologicalContext(question, ecologicalContext)
                    if (!existing) {
                        existing = new MatrixValue(question: question, ecologicalContext: ecologicalContext)
                        existing.save(flush: true, failOnError: true)
                        results.matrixValuesAdded++
                    }
                    existing.required = kvp.value as Boolean
                }

                flash.message = results.toString()
            }
        }

        redirect(action: 'matrix')

    }

    def dataExtractions() {

        params.max = params.max ?: 10
        params.sort= params.sort ?: "date"
        params.order= params.order ?: "desc"

        def extractions = DataExtraction.list(params)

        [extractions: extractions]
    }

    def deleteDataExtraction() {
        def extract = DataExtraction.findByPackageName(params.packageName)
        if (extract) {
            extract.delete()
            flash.message = "Data extraction package ${params.packageName} deleted"
        } else {
            flash.errorMessafge = "Package name missing or package not found!"
        }
        redirect(action:'dataExtractions')
    }

}
