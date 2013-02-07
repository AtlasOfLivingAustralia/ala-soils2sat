package au.org.ala.soils2sat

import groovy.xml.MarkupBuilder
import org.apache.commons.lang.WordUtils

import java.text.SimpleDateFormat
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 *
 */
class S2STagLib {

    static namespace = 'sts'

    def springSecurityService
    def layerService

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
            def mb = new MarkupBuilder(out)
            switch (criteriaDefinition.valueType) {
                case CriteriaValueType.StringDirectEntry:
                    mb.strong {
                        mkp.yield("Enter a value (or values seperated by '|') to match:")
                    }
                    mb.div {
                        input(type:'text', name:'fieldValue', class:"input-xlarge") {}
                    }
                    break;
                case CriteriaValueType.NumberRangeDouble:
                case CriteriaValueType.NumberRangeInteger:
                    mb.label(class:'radio inline', style:'white-space: nowrap') {
                        mb.input(type:'radio', name:'operator', value: 'gt', checked:'checked') {
                        }
                        mkp.yield("Greater than or equal to")
                    }
                    mb.label(class:'radio inline', style:'white-space: nowrap') {
                        mb.input(type:'radio', name:'operator', value: 'lt') {
                            mkp.yield("Less than or equal to")
                        }
                    }
                    mb.input(type: "text", placeholder:'Value', class:'input-small', style: "margin-left: 20px")
                    if (attrs.units) {
                        mb.span {
                            mkp.yieldUnescaped("&nbsp;(")
                            mkp.yield(attrs.units + ")")
                        }
                    }
                    break;
                default:
                    mb.div {
                        mkp.yield("Unhandled input type: " + criteriaDefinition.valueType.toString())
                    }
                    break;
            }
        }
//        <g:textField name="fieldValue" class="input-xlarge" />
    }

}
