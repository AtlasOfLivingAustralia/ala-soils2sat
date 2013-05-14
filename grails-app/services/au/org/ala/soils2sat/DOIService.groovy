package au.org.ala.soils2sat

import groovy.xml.MarkupBuilder
import groovyx.net.http.HTTPBuilder

class DOIService extends ServiceBase {

    def grailsApplication
    def grailsLinkGenerator

    def mintDOI(DataExtraction extraction, User user) {
        if (!extraction) {
            throw new RuntimeException("Extraction package is null!")
        }

        def landingPageUrl = grailsApplication.config.doiS2SRoot + grailsLinkGenerator.link(controller: 'extract', action:'landingPage', params:[packageName: extraction.packageName], absolute: false)

        def mintingUrl = makeDOIUrl('create', [:]) // [url:landingPageUrl]
        def postBody = buildRequestXML(user.userProfile.fullName, "Data extract ${extraction.packageName}", [] )
        try {

            def http = new HTTPBuilder(mintingUrl)

            http.post(body: postBody) { resp ->
                println resp
            }

        } catch (Exception ex) {
            println "Minting URL: ${mintingUrl}"
            println "RequestPayload: ${postBody}"

            ex.printStackTrace()
            println ex
            throw new DOIMintingFailedException(ex.message)
        }

        return 'The DOI!'
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

    public String buildRequestXML(String doiCreator, String doiTitle, List<String> doiSubjects) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.mkp.xmlDeclaration version: '1.0', encoding: 'UTF-8'
        xml.resource(
            xmlns:'http://datacite.org/schema/kernel-2.1',
            'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance',
            'xsi:schemaLocation':'http://datacite.org/schema/kernel-2.1 http://schema.datacite.org/meta/kernel-2.1/metadata.xsd') {
            identifier(identifierType:'DOI') {}
            creators() {
                creator() {
                    creatorName(doiCreator)
                }
            }
            titles() {
                title(doiTitle)
            }
            publisher('Atlas of Living Australia')
            publicationYear('2013')
            subjects {
                for (String doiSubject : doiSubjects) {
                    subject(subjectScheme:'Subject',doiSubject)
                }
            }
        }


        return writer.toString()
    }


    @Override
    protected String getServiceRootUrl() {
        return "${grailsApplication.config.doiServiceRoot}"
    }
}
