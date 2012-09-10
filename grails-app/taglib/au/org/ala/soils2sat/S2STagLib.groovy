package au.org.ala.soils2sat

import groovy.xml.MarkupBuilder

/**
 *
 */
class S2STagLib {

    static namespace = 'sts'

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

}
