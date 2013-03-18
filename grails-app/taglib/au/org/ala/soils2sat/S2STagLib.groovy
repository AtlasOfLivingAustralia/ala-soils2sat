package au.org.ala.soils2sat

import groovy.xml.MarkupBuilder
import org.apache.commons.lang.WordUtils
import org.codehaus.groovy.grails.web.pages.discovery.GrailsConventionGroovyPageLocator
import org.codehaus.groovy.grails.web.pages.discovery.GroovyPageLocator

import java.text.SimpleDateFormat
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 *
 */
class S2STagLib {

    static namespace = 'sts'

    def springSecurityService
    def layerService
    def groovyPageLocator

    /**
     * @attr active
     */
    def navbar = { attrs ->

        def mb = new MarkupBuilder(out)

        mb.ul(class:'nav') {
            li(class:attrs.active == 'home' ? 'active' : '') {
                a(href:createLink(uri: '/')) { mkp.yield(message(code:'default.home.label', default: 'Home'))}
            }
            li(class:attrs.active == 'about' ? 'active' : '') {
                a(href:createLink(controller: 'about')) { mkp.yield(message(code:'default.about.label', default: 'About'))}
            }
        }

    }

    /**
     * @attr layer
     */
    def layerTreeItem = { attrs ->
        LayerDefinition layer = attrs.layer as LayerDefinition

        if (!layer) {
            return
        }

        def mb = new MarkupBuilder(out)
        mb.li('layerName': layer.name) {
            mkp.yield(layer.displayname)
            small {
                mkp.yield(layer.description)
            }
        }
    }

    def ifAdmin = { attrs, body ->

        def user = springSecurityService.currentUser as User
        if (user) {
            def adminRole = UserRole.findByUserAndRole(user, Role.findByAuthority('ROLE_ADMIN'))
            if (adminRole) {
                out << body()
            }
        }

    }

    /**
     * @attr user
     */
    def roles = { attrs, body ->
        def user = attrs.user as User
        if (!user) {
            user = springSecurityService.currentUser as User
        }

        if (user) {
            def roles = UserRole.findAllByUser(user)
            def roleNames = roles.collect {
                it.role.authority
            }
            out << roleNames.join(", ")
        }
    }

    /**
     * @attr date
     */
    def formatDateStr =  { attrs, body ->

        Date theDate = null

        if (attrs.date) {
            if (attrs.date instanceof String) {
                theDate = DateUtils.tryParse(attrs.date)
            } else if (attrs.date instanceof Date) {
                theDate = attrs.date
            }
            if (theDate) {
                SimpleDateFormat sdf = null
                if (params.dateFormat) {
                    sdf = new SimpleDateFormat(params.dateFormat)
                } else {
                    sdf = new SimpleDateFormat("dd/MM/yyyy")
                }
                out << sdf.format(theDate)
                return
            }
        }
    }

    /**
     * @attr active
     * @attr title
     * @attr href
     */
    def breadcrumbItem = { attrs, body ->
        def active = attrs.active
        if (!active) {
            active = attrs.title
        }
        def current = pageProperty(name:'page.pageTitle')?.toString()

        def mb = new MarkupBuilder(out)
        mb.li(class: active == current ? 'active' : '') {
            a(href:attrs.href) {
                i(class:'icon-chevron-right') { mkp.yieldUnescaped('&nbsp;')}
                mkp.yield(attrs.title)
            }
        }
    }

    /**
     * @attr code
     */
    def formatSamplingUnitName = { attrs, body ->
        def code = attrs.code as String
        if (code) {
            out << WordUtils.capitalizeFully(code.replaceAll('_', ' '))
        }
    }

    /**
     * @attr layerName
     */
    def layerDisplayName = { attrs, body ->
        def layerName = attrs.layerName
        def href = attrs.href

        def mb = new MarkupBuilder(out)

        if (layerName) {
            def layerInfo = layerService.getLayerInfo(layerName)
            if (href) {
                mb.a(href:href, title:layerInfo?.description, class:attrs.class, layerName: layerName) {
                    mkp.yield(layerInfo?.displayname ?: layerName)
                }
            } else {
                out << layerInfo?.displayname ?: layerName
            }

        }
    }

    /**
     * @attr key
     * @attr value
     */
    def layerMetadataValue = { attrs, body ->

        def key = attrs.key
        def value = attrs.value

        def mb = new MarkupBuilder(out)
        if (value instanceof String) {
            def str = value as String

            if (str.matches("^http[s]{0,1}[:]//.+\$")) {
                mb.a(href: str, target: 'other') {
                    mkp.yield(str)
                }
            } else {
                out << str
            }
        } else {
            out << value
        }

    }

    def spinner = { attrs, body ->
        def mb = new MarkupBuilder(out)
        mb.img(src: resource(dir:'/images', file:'spinner.gif')) {

        }
    }

    /**
     * @attr criteriaDefinition
     * @attr units
     */
    def criteriaValueControl = { attrs, body ->
        def criteriaDefinition = attrs.criteriaDefinition as SearchCriteriaDefinition
        if (criteriaDefinition) {
            def templateName = criteriaDefinition.valueType.toString()
            def templatePath = '/criteriaControls/' + templateName[0].toLowerCase() + templateName.substring(1)
            println templatePath
            if (groovyPageLocator) {
                if (!groovyPageLocator.findTemplateByPath(templatePath)) {
                    throw new Exception("Could not locate template for criteria value type: " + criteriaDefinition.valueType.toString())
                }
            }

            def allowedValues = []
            if (criteriaDefinition.valueType == CriteriaValueType.StringMultiSelect && (criteriaDefinition.type == CriteriaType.StudyLocationVisit || criteriaDefinition.type == CriteriaType.StudyLocation)) {
                allowedValues = []
            }

            out << render(template: templatePath, model: [criteriaDefinition: criteriaDefinition, units: attrs.units, value: attrs.value, allowedValues: allowedValues])
        }
    }

    /**
     * @attr samplingUnit
     * @attr visitDetail
     */
    def renderSamplingUnit = { attrs, body ->

        def samplingUnit = attrs.samplingUnit
        def visitDetail = attrs.visitDetail as Map

        if (visitDetail && samplingUnit) {
            def dataList = []
            switch (samplingUnit) {
                case "POINT_INTERCEPT":
                    dataList = visitDetail.pointInterceptWithHerbIdAddedList
                    break
                case "STRUCTURAL_SUMMARY":
                    dataList = visitDetail.structuralSummaryList
                    break
                case "SOIL_STRUCTURE":
                    dataList = visitDetail.soilStructureList
                    break
                case "SOIL_CHARACTER":
                    dataList = visitDetail.soilCharacterisationList
                    break
                case "SOIL_SAMPLING":
                    dataList = visitDetail.soilSampleList
                    break
                default:
                break;
            }
            def mb = new MarkupBuilder(out)
            if (dataList) {
                def colHeaders = dataList[0].collect { it.key }

                mb.ul(class: "samplingUnitTree") {
                    li {
                        div(class:'samplingUnitTitle') {
                            mkp.yield(samplingUnit)
                        }
                        table(class:'table table-striped table-bordered table-condensed') {
                            thead {
                                tr {
                                    for (def col : colHeaders) {
                                        th {
                                            mkp.yield(col)
                                        }
                                    }
                                }
                            }
                            tbody {
                                for (def row : dataList) {
                                    tr {
                                        for (def col : colHeaders) {
                                            td {
                                                mkp.yield(row[col] ?: '')
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                mb.span() {
                    mkp.yield("Sampling unit details not found for '" + title + "'")
                }
            }
        }

    }

}
