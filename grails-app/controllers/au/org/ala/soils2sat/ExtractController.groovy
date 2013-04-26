package au.org.ala.soils2sat

class ExtractController {

    def transient springSecurityService
    def transient extractService

    def index() {
        def user = springSecurityService.currentUser as User
        if (user) {
            redirect(action:'extractData')
            return
        }
        flash.message = "No user!"
        redirect(controller: 'map', action:'index')
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
            on("success").to "showVisits"
        }

        def isCollection = { object ->
            [Collection, Object[]].any { it.isAssignableFrom(object.getClass()) }
        }

        showVisits {
            on("continue") {
                def selected = params.get("visitId")
                def selectedVisitIds = []
                if (!selected) {
                    flow.selectedVisitIds = selectedVisitIds
                    flash.errorMessage = "You must select at least on visit to extract data for"
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
            on("cancel").to "cancel"
        }

        selectSamplingUnits {
            on("continue").to "extractAndPackage"
            on("back").to "showVisits"
            on("cancel").to "cancel"
        }

        extractAndPackage {
            action {
                def user = springSecurityService.currentUser as User
                def selectedVisitIds = flow.selectedVisitIds as List<String>
                def results = extractService.extractAndPackageData(user, selectedVisitIds, null)
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

        if (!extraction) {
            flash.errorMessage = "No package name specified, or package does not exists: ${packageName}"
        } else {
            def file = new File(extraction.localFile)
            if (file.exists()) {
                filesize = file.length()
                filename = file.name
            }
        }


        [extraction: extraction, filesize: filesize, filename: filename]
    }

}
