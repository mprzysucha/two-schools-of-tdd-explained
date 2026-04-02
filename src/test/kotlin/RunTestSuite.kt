import org.property.calculator.PropertyPriceCalculatorTest
import org.property.reports.PropertyPriceReportOrchestratorTest
import org.property.tooling.SuiteRunner.runTests

object RunTestSuite {
    @JvmStatic
    fun main(args: Array<String>) {
        val propertyPriceCalculatorSuite = PropertyPriceCalculatorTest()
        runTests(listOf(
            propertyPriceCalculatorSuite::testWithStub,
            propertyPriceCalculatorSuite::testWithDummy,
            propertyPriceCalculatorSuite::testWithSpy,
        ))

        val propertyPriceReportOrchestratorSuite = PropertyPriceReportOrchestratorTest()
        runTests(listOf(
            propertyPriceReportOrchestratorSuite::testWithMock,
            propertyPriceReportOrchestratorSuite::testWithFake,
        ))
    }

}