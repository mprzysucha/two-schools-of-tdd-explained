import org.property.calculator.PropertyPriceCalculatorTest
import org.property.reports.PropertyPriceReportOrchestratorTest
import kotlin.reflect.KFunction0

object RunTestSuite {
    @JvmStatic
    fun main(args: Array<String>) {
        val propertyPriceCalculatorSuite = PropertyPriceCalculatorTest()
        runTests(listOf(
            propertyPriceCalculatorSuite::testWithStub,
            propertyPriceCalculatorSuite::testWithDummy,
            propertyPriceCalculatorSuite::testWithSpy,
            propertyPriceCalculatorSuite::testWithFake,
        ))

        val propertyPriceReportOrchestratorSuite = PropertyPriceReportOrchestratorTest()
        propertyPriceReportOrchestratorSuite.testReports()
        runTests(listOf(
            propertyPriceReportOrchestratorSuite::testReports,
        ))
    }

    private fun runTests(tests: List<KFunction0<Unit>>, successMessage: () -> Any = { "SUCCESS" }) {
        for (test in tests) {
            test().also {
                println("${test.name} ${successMessage()}")
            }
        }
    }
}