package au.org.ala.soils2sat

import groovy.xml.MarkupBuilder
import org.apache.commons.lang.WordUtils
import java.text.SimpleDateFormat

/**
 *
 */
class S2STagLib {

    static namespace = 'sts'

    def springSecurityService
    def layerService
    def groovyPageLocator
    def studyLocationService

    /**
     * @attr active
     */
    def navbar = { attrs ->

        def mb = new MarkupBuilder(out)

        mb.ul(class:'nav') {
            li(class:attrs.active == 'home' ? 'active' : '') {
                    a(href:createLink(uri: '/')) {
                        i(class:"icon-home") {
                            mkp.yieldUnescaped("&nbsp;")
                        }
                        mkp.yieldUnescaped("&nbsp")
                        mkp.yield(message(code:'default.home.label', default: 'Home'))}
            }
            li(class:attrs.active == 'about' ? 'active' : '') {
                a(href:createLink(controller: 'about')) {
                    i(class:"icon-question-sign") {
                        mkp.yieldUnescaped("&nbsp;")
                    }
                    mkp.yieldUnescaped("&nbsp")
                    mkp.yield(message(code:'default.about.label', default: 'About'))
                }
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
    def formatSamplingUnitColumn = { attrs, body ->
        def code = attrs.code as String
        if (code) {
            out << StringUtils.makeTitleFromCamelCase(code)
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
     * @attr studyLocationVisitId
     */
    def formatVisitLabel = { attrs, body ->

        def studyLocationVisitId = attrs.studyLocationVisitId
        if (studyLocationVisitId) {
            def visitDetails = studyLocationService.getVisitDetails(studyLocationVisitId)
            if (visitDetails) {
                def mb = new MarkupBuilder(out)
                mb.span() {
                    mkp.yield(visitDetails?.visitStartDate)
                }
            }
        }

    }

    def navSeperator = { attrs, body ->
        out << "&nbsp;&#187;&nbsp;"
    }

    def loading = { attrs, body ->
        def message = attrs.message ?: "Loading..."
        def mb = new MarkupBuilder(out)
        mb.span() {
            mb.img(src:resource(dir: '/images', file:'spinner.gif'))
            mkp.yieldUnescaped("&nbsp;")
            mkp.yield(message)
        }
    }

    def taxaHomePageLink = { attrs, body ->
        def name = attrs.name
        def mb = new MarkupBuilder(out)
        if (name) {
            mb.a(href:"http://bie.ala.org.au/species/${name}", target:'ala-window') {
                span(class:'taxanomicName') { mkp.yield(name) }
                mb.img(src:resource(dir:'images', file:'external-link.png'))
            }
        } else {
            if (attrs.ifEmpty) {
                mb.span(class:'muted') {
                    mkp.yield(attrs.ifEmpty)
                }
            }
        }
    }

    def renderSamplingUnitValue = { attrs, body ->
        def mb = new MarkupBuilder(out)
        def value = attrs.value?.toString()?.trim()
        if (value) {
            mb.span() {
                mkp.yield(value)
            }
        } else {
            mb.span(class:'muted') {
                mkp.yield("N/A")
            }
        }
    }

}
