package au.org.ala.soils2sat



import grails.test.mixin.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(SearchCriteria)
class SearchCriteriaTests {

    void testCriteriaEval1() {
        def eval = SearchCriteriaUtils.newDoubleEvaluator("gt 200.1")
        assert eval.evaluate(300.1231);
    }

    void testCriteriaEval2() {
        def eval = SearchCriteriaUtils.newDoubleEvaluator("gt 400")
        assert !eval.evaluate(300);
    }

    void testCriteriaEval3() {
        def eval = SearchCriteriaUtils.newDoubleEvaluator("gt 200")
        assert eval.evaluate(200);
    }

    void testCriteriaEval4() {
        def eval = SearchCriteriaUtils.newDoubleEvaluator("lt 200")
        assert eval.evaluate(100);
    }

    void testCriteriaEval5() {
        def eval = SearchCriteriaUtils.newDoubleEvaluator("lt 400")
        assert !eval.evaluate(500);
    }

    void testCriteriaEval6() {
        def eval = SearchCriteriaUtils.newDoubleEvaluator("lt 200")
        assert eval.evaluate(200);
    }

    void testFoo() {
        println new SearchCriteriaUtils.MultiStringPatternTranslator("A|B|C").displayString()
    }

}
