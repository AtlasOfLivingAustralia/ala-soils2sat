package au.org.ala.soils2sat



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(DOIService)
class DOIServiceTests {

    void testSomething() {
        println service.buildRequestXML('creatorName', 'title', ['ALA', 'Soils2Sat', 'AEKOS', 'AUSPLOTS'])
    }
}
