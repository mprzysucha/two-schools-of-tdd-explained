package org.property.reports

import org.property.calculator.City
import org.property.calculator.City.Companion.WRO
import org.property.calculator.EmailSender
import org.property.calculator.PropertyPriceCalculator
import org.property.email.EmailData

class PropertyPriceReportOrchestratorTest {

    class OrderRegister() {
        val registeredCalls = mutableListOf<String>()
        fun methodCalled(order: Int, id: String): Boolean {
            return order > 0 && order <= registeredCalls.size && registeredCalls[order - 1] == id
        }
        fun registerCall(id: String) {
            registeredCalls.add(id)
        }
    }

    open class OrderedMocks(val id: String, val orderRegister: OrderRegister) {
        open fun verifyMethodCallInOrder(order: Int) {
            if (!orderRegister.methodCalled(order, id)) {
                throw AssertionError("verifyMethodCallInOrder for id \"$id\" FAILED, Actual order: ${orderRegister.registeredCalls.indexOf(id) + 1}, Expected order: $order")
            }
        }
        protected fun registerCall() {
            orderRegister.registerCall(id)
        }
    }

    private val orderRegister = OrderRegister()
    private val propertyPriceCalculatorMock = object : PropertyPriceCalculator, OrderedMocks("price", orderRegister) {
        override fun price(area: Int, rooms: Int, city: City): Int {
            registerCall()
            return 11800
        }
    }

    private val propertyPriceReportTemplatesMock = object : PropertyPriceReportTemplates, OrderedMocks("template", orderRegister) {
        override fun generatePricesReport(template: ReportTemplate, reportInputData: ReportInputData): Report {
            registerCall()
            return Report("This is report")
        }
    }

    private val emailSenderMock = object : EmailSender, OrderedMocks("email", orderRegister) {
        override fun sendEmail(to: String, subject: String, body: String) {
            registerCall()
        }
    }

    fun testReports() {
        //given
        val systemUnderTest = PropertyPriceReportOrchestrator(propertyPriceCalculatorMock, propertyPriceReportTemplatesMock, emailSenderMock)
        val template = ReportTemplate()
        val reportInputData = ReportInputData(area = 50, rooms = 2, city = WRO)
        val emailData = EmailData(to = "customer@company.com", subject = "Property price report")

        //when
        systemUnderTest.generatePricesReportAndSend(template, reportInputData, emailData)

        //then
        propertyPriceCalculatorMock.verifyMethodCallInOrder(order = 1)
        propertyPriceReportTemplatesMock.verifyMethodCallInOrder(order = 2)
        emailSenderMock.verifyMethodCallInOrder(order = 3)
    }
}

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val testSuite = PropertyPriceReportOrchestratorTest()
        testSuite.testReports()
    }
}