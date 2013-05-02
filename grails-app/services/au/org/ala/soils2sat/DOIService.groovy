package au.org.ala.soils2sat

class DOIService extends ServiceBase {

    def grailsApplication

    def mintDOI() {

    }

    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.doiServiceRoot}"
    }
}
