package au.org.ala.soils2sat

class ExtractController {

    def transient springSecurityService
    def transient extractService
    def transient studyLocationService

    def index() {
        def user = springSecurityService.currentUser as User
        if (!user) {
            flash.message = "No user!"
            redirect(controller: 'map', action:'index')
            return
        }

        redirect(action:'extractData')
    }

    def extractDataFlow = {

        initialize {
            action {
                def user = springSecurityService.currentUser as User

                def appState = user.applicationState
                def selectedVisitIds = []
                appState?.selectedVisits?.each {
                    selectedVisitIds << it.studyLocationVisitId
                }
                flow.selectedVisitIds = selectedVisitIds
                [appState: appState, user: user]
            }
            on("success").to "startPage"
        }

        def isCollection = { object ->
            [Collection, Object[]].any { it.isAssignableFrom(object.getClass()) }
        }

        startPage {
            on("continue").to "showVisits"
            on("cancel").to "cancel"
        }

        showVisits {
            on("continue") {
                def selected = params.get("visitId")
                def selectedVisitIds = []
                if (!selected) {
                    flow.selectedVisitIds = selectedVisitIds
                    flash.errorMessage = "You must select at least one visit to extract data for"
                    return error()
                }

                if (selected instanceof String) {
                    selectedVisitIds << selected
                } else if (isCollection(selected)) {
                    selected.each {
                        selectedVisitIds << it
                    }
                }
                flow.selectedVisitIds = selectedVisitIds
            }.to "selectSamplingUnits"
            on("back").to "startPage"
            on("cancel").to "cancel"
        }

        selectSamplingUnits {
            onEntry {
                flow.availableSamplingUnits = studyLocationService.getAvailableSamplingUnits(flow.selectedVisitIds)
                if (!flow.selectedSamplingUnits) {
                    flow.selectedSamplingUnits = flow.availableSamplingUnits*.id
                }
            }
            on("continue").to("processSamplingUnits")

            on("themedExtract").to "themeQuestions"
            on("back").to "showVisits"
            on("cancel").to "cancel"
        }

        processSamplingUnits {
            action {
                def selected = params.get("samplingUnitId")
                def selectedUnitIds = []
                flow.samplingUnits = params.samplingUnits

                if (flow.samplingUnits == 'matrix') {
                    return matrix()
                }

                if (!selected) {
                    flow.selectedSamplingUnits = selectedUnitIds
                    flash.errorMessage = "You must select at least one sampling unit to extract data for"
                    return error()
                }

                if (selected instanceof String) {
                    selectedUnitIds << selected
                } else if (isCollection(selected)) {
                    selected.each {
                        selectedUnitIds << it
                    }
                }
                flow.selectedSamplingUnits = selectedUnitIds
                return nonMatrix()
            }
            on("matrix").to("themeQuestions")
            on("nonMatrix").to("citationDetails")
        }

        themeQuestions {
            onEntry {
                def questions = Question.list().sort { it.id }
                [questions: questions]
            }

            on("continue") {
                def selected = params.get("questionId")
                def selectedQuestionIds = []
                if (selected instanceof String) {
                    selectedQuestionIds << selected
                } else if (isCollection(selected)) {
                    selected.each { selectedQuestionIds << it }
                }

                if (!selectedQuestionIds) {
                    flash.errorMessage ="Please select at least one question you are interested in"
                    return error()
                }

                flow.selectedQuestionIds = selectedQuestionIds
            }.to "pointsOfView"
            on("back").to "selectSamplingUnits"
            on("cancel").to "cancel"
        }

        pointsOfView {
            onEntry {
                def questions = []
                def contextMap = [:]
                flow.selectedQuestionIds?.each {
                    def question = Question.get(it)
                    questions << question
                    def matrixValues = MatrixValue.findAllByQuestionAndRequired(question, true)
                    def contexts = matrixValues*.ecologicalContext
                    contextMap[question] = contexts
                }
                [questions: questions, contextMap: contextMap]
            }
            on("continue").to "themeSamplingUnits"
            on("back").to "themeQuestions"
            on("cancel").to "cancel"
        }

        themeSamplingUnits {
            on("continue").to "citationDetails"
            on("back").to "pointsOfView"
            on("cancel").to "cancel"
        }

        citationDetails {
            onEntry {
                def user = springSecurityService.currentUser as User
                if (user.userProfile) {
                    if (!flow.creatorSurname) {
                        flow.creatorSurname = user.userProfile.surname
                    }
                    if (!flow.creatorGivenNames) {
                        flow.creatorGivenNames = user.userProfile.givenNames
                    }
                }
            }

            on("continue") {

                flow.creatorSurname = params.get("surname")
                flow.creatorGivenNames = params.get("givenNames")

            }.to "extractAndPackage"
            on("back").to { flow.samplingUnits == 'matrix' ? "themeSamplingUnits" : "selectSamplingUnits" }
            on("cancel").to "cancel"
        }

        extractAndPackage {
            action {
                def user = springSecurityService.currentUser as User
                def selectedVisitIds = flow.selectedVisitIds as List<String>
                def selectedSamplingUnits = flow.selectedSamplingUnits
                def results = extractService.extractAndPackageData(user, selectedVisitIds, selectedSamplingUnits)
                flow.extractionResults = results
            }

            on("success").to "extractionResults"
        }

        extractionResults {
            on("finish").to "finish"
        }

        cancel {
            redirect(controller:'map', action:"index")
        }

        finish {
            redirect(controller:'map', action:"index")
        }

    }

    def downloadPackage() {
        def packageName = params.packageName

        if (!packageName) {
            // TODO: return a HTTP error code
            return
        }

        def extract = DataExtraction.findByPackageName(packageName)
        if (!extract) {
            // TODO:return HTTP error code
            return
        }

        File f = new File(extract.localFile)
        if (!f.exists()) {
            // TODO: return HTTP error code
            return
        }

        extract.downloadCount = (extract.downloadCount ?: 0) + 1

        response.setHeader("Content-Disposition", "attachment;filename=" + packageName +".zip");
        response.setContentType("application/zip");

        // write the file contents to the output stream
        response.outputStream << f.newInputStream()



    }

    def landingPage() {
        def packageName = params.packageName

        def extraction = DataExtraction.findByPackageName(packageName)

        def filesize = 0
        def filename = ""
        def manifestText = ""
        User author = null

        if (!extraction) {
            flash.errorMessage = "No package name specified, or package does not exist: ${packageName}"
        } else {
            def file = new File(extraction.localFile)
            if (file.exists()) {
                filesize = file.length()
                filename = file.name
                manifestText = extractService.extractManifest(file)
            }
            author = User.findByUsername(extraction.username)
        }


        [extraction: extraction, filesize: filesize, filename: filename, manifestText: manifestText, author: author]
    }

}
