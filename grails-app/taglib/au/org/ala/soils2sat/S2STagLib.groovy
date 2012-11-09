package au.org.ala.soils2sat

import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat

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
     * @attr dateStr
     */
    def formatDateStr =  { attrs, body ->
        def sdf = new SimpleDateFormat("yyyy-MM-dd")
        if (attrs.dateStr) {
            def date = sdf.parse(attrs.dateStr)
            if (params.dateFormat) {
                sdf = new SimpleDateFormat(params.dateFormat)
            } else {
                sdf = new SimpleDateFormat("dd MMM, yyyy")
            }
            out << sdf.format(date)
        }
    }

    /**
     * @attr id
     * @attr title
     */
    def modalDialog = { attrs, body ->

        def mb = new MarkupBuilder(out)
        def height = attrs.height ?: 400
        def width = attrs.width ?: 600

        mb.div(id: attrs.id, class:"modal hide fade", role:"dialog", 'aria-labelledby':"modal_label_${attrs.id}", 'aria-hidden': 'true', style:"height: ${height}px;width: ${width}px; overflow: hidden") {
            div(class:'modal-header') {
                button(type:'button', class:'close', 'data-dismiss':'modal', 'aria-hidden':'true') {
                    mkp.yield('x')
                }
                h3(id:"modal_label_${attrs.id}") {
                   mkp.yield(attrs.title)
                }
            }
            div(class:'modal-body', style:"max-height: ${height}px") {
                mkp.yield('loading')
            }
        }
    }

}
