/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.soils2sat

import au.edu.aekos.shared.doiclient.jaxb.DateType
import au.edu.aekos.shared.doiclient.jaxb.Resource
import au.edu.aekos.shared.doiclient.service.DoiClientConfig
import au.edu.aekos.shared.doiclient.service.DoiClientService
import au.edu.aekos.shared.doiclient.service.DoiClientServiceException
import au.edu.aekos.shared.doiclient.service.DoiClientServiceImpl
import au.edu.aekos.shared.doiclient.util.ResourceBuilder

class DOIService {

    static transactional = false

    def grailsApplication
    def grailsLinkGenerator
    def settingService

    private DoiClientService getDoiClientService() {
        def svc = new DoiClientServiceImpl()
        def config = new DoiClientConfig()
        def cfg = grailsApplication.config

        config.doiMintingServiceUrl = settingService.DOIServiceUrl
        config.appId = settingService.DOIServiceAppId
        config.userId = settingService.DOIServiceUsername
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
        } catch (DoiClientServiceException doiEx) {
            throw new DOIMintingFailedException(doiEx.message)
        } catch (Exception ex) {
            throw new DOIMintingFailedException(ex.message)
        }

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

}
