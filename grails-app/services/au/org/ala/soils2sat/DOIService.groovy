package au.org.ala.soils2sat

import au.edu.aekos.shared.doiclient.jaxb.DateType
import au.edu.aekos.shared.doiclient.jaxb.Resource
import au.edu.aekos.shared.doiclient.service.DoiClientConfig
import au.edu.aekos.shared.doiclient.service.DoiClientService
import au.edu.aekos.shared.doiclient.service.DoiClientServiceImpl
import au.edu.aekos.shared.doiclient.util.ResourceBuilder
import groovy.xml.MarkupBuilder
import groovyx.net.http.HTTPBuilder

class DOIService extends ServiceBase {

    static transactional = false

    def grailsApplication
    def grailsLinkGenerator

    private DoiClientService getDoiClientService() {
        def svc = new DoiClientServiceImpl()
        def config = new DoiClientConfig()
        def cfg = grailsApplication.config

        config.doiMintingServiceUrl = cfg.doiServiceRoot
        config.appId = cfg.doiAppId
        config.userId = cfg.doiUserId
        config.topLevelUrl = cfg.doiS2SRoot
        config.keystoreFilePath = this.getClass().getResource(cfg.doiKeystorePath).path
        config.keystorePassword = cfg.doiKeystorePassword
        svc.setDoiClientConfig(config)
        return svc
    }

    def mintDOI(DataExtraction extraction, User user) {
        if (!extraction) {
            throw new RuntimeException("Extraction package is null!")
        }

        def landingPageUrl = grailsApplication.config.doiS2SRoot + grailsLinkGenerator.link(controller: 'extract', action:'landingPage', params:[packageName: extraction.packageName], absolute: false)


        try {
            def doi = doiClientService.mintDoi(buildCreateXML(user.userProfile.fullName, "Data extract ${extraction.packageName}", []), landingPageUrl)
            return doi
        } catch (Exception ex) {
            ex.printStackTrace()
            println ex
            throw new DOIMintingFailedException(ex.message)
        }
    }

    private String makeDOIUrl(String api, Map params) {
        def cfg = grailsApplication.config
        def serviceUrl = cfg.doiServiceRoot + "index.php?r=api/${api}&user_id=${cfg.doiUserId}&app_id=${cfg.doiAppId}"
        if (params) {
            params.keySet().each {
                serviceUrl += "&${it}=${params[it]}"
            }
        }
        return serviceUrl
    }

    public Resource buildCreateXML(String doiCreator, String doiTitle, List<String> doiSubjects) {
        ResourceBuilder builder = new ResourceBuilder();
        builder.addCreator(doiCreator).setTitle(doiTitle).setPublisher("Atlas of Living Australia").setPublicationYear("2013")        		       
      		       .addDate(new Date(), DateType.CREATED).addDate(new Date(), DateType.START_DATE);

        doiSubjects?.each {
            builder.addSubject(it)
        }

        return builder.resource
    }


    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.doiServiceRoot}"
    }
}
