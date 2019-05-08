grails.servlet.version = "2.5" /*
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
 */ // Change depending on target container compliance (2.5 or 3.0)

//grails.project.class.dir = "target/classes"
//grails.project.test.class.dir = "target/test-classes"
//grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = 'target'

grails.project.groupId = "au.org.ala"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.project.war.file = "target/${appName}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        mavenRepo "http://repo.grails.org/grails/core"
        mavenRepo "http://repo.grails.org/grails/plugins"

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.20'
        runtime 'postgresql:postgresql:9.1-901.jdbc4'

        compile 'org.apache.httpcomponents:httpclient:4.2.4'
        compile 'org.imgscalr:imgscalr-lib:4.2'
        compile 'org.apache.cxf:cxf-rt-frontend-jaxrs:2.7.5'
        compile 'org.apache.cxf:cxf-rt-rs-extension-search:2.7.5'
        runtime 'org.apache.pdfbox:fontbox:1.8.2'
        compile 'org.apache.pdfbox:pdfbox:1.8.2'
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.8.3"
        compile ":jquery-ui:1.8.24"
        runtime ":resources:1.2.RC2"
        compile (":twitter-bootstrap:2.3.2") { excludes 'svn' }

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.4"

        build ":tomcat:$grailsVersion" // if migrating to grails 2.5.3 change $grailsVersion to v7.0.70

        runtime ":database-migration:1.3.2"
        compile ':cache:1.0.1'
        compile ':csv:0.3.1'
        compile ':spring-security-core:1.2.7.3'
        compile (':webflow:2.0.8.1') {
            excludes 'javassist'
        }
        compile ':rest:0.7'
        compile ":google-visualization:0.6.2"

    }
}
