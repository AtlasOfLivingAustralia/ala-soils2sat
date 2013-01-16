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

}
