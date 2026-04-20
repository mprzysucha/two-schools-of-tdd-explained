package org.property.reports

import org.property.calculator.City
import org.property.calculator.City.Companion.WRO
import org.property.calculator.EmailSender
import org.property.calculator.MarketPricesProvider
import org.property.calculator.PropertyPriceCalculator
import org.property.calculator.RealPropertyPriceCalculator
import org.property.email.Email
import org.property.email.EmailMetadata
import org.property.tooling.Assert.assertEquals
import org.property.tooling.Assert.assertThat

class PropertyPriceReportOrchestratorTest {



    fun testWithStub() {
        //given
        val marketPricesProviderStub = object : MarketPricesProvider {
            override fun providePrice(city: City): Int = when(city) {
                WRO -> 13200
                else -> 11800
            }
        }
        val propertyPriceReportTemplates = object : PropertyPriceReportTemplates { }
        val propertyPriceCalculator = RealPropertyPriceCalculator(marketPricesProviderStub)
        data class Email(val to: String, val subject: String, val body: String)
        var emailSent: Email? = null
        val emailSenderStub = object : EmailSender {
            override fun sendEmail(to: String, subject: String, body: String) {
                emailSent = Email(to, subject, body)
            }
        }

        //given
        val propertyPriceReportOrchestrator = PropertyPriceReportOrchestrator(propertyPriceCalculator, propertyPriceReportTemplates, emailSenderStub)
        val template = SimpleTextReportTemplate()
        val reportInputData = ReportInputData(area = 60, rooms = 2, city = WRO)
        val emailMetadata = EmailMetadata(to = "customer@company.com", subject = "Property price report")

        //when
        propertyPriceReportOrchestrator.generatePricesReportAndSend(template, reportInputData, emailMetadata)

        //then
        assertThat(emailSent != null)
        assertEquals(emailSent!!.to, "customer@company.com")
        assertEquals(emailSent.subject, "Property price report")
        assertEquals(emailSent.body, "The property in Wroclaw with 2 rooms having 60 m2 costs 792000.")
    }

    fun testWithMock() {
        //given
        val propertyPriceReportOrchestrator = PropertyPriceReportOrchestrator(propertyPriceCalculatorMock, propertyPriceReportTemplatesMock, emailSenderMock)
        val template = SimpleTextReportTemplate()
        val reportInputData = ReportInputData(area = 50, rooms = 2, city = WRO)
        val emailMetadata = EmailMetadata(to = "customer@company.com", subject = "Property price report")

        //when
        propertyPriceReportOrchestrator.generatePricesReportAndSend(template, reportInputData, emailMetadata)

        //then
        propertyPriceCalculatorMock.verifyMethodCallInOrder(order = 1)
        propertyPriceReportTemplatesMock.verifyMethodCallInOrder(order = 2)
        emailSenderMock.verifyMethodCallInOrder(order = 3)
    }

    fun testWithFake() {
        //given
        val propertyPriceReportOrchestrator = PropertyPriceReportOrchestrator(propertyPriceCalculatorDummy, propertyPriceReportTemplatesStub, emailSenderFake)
        val reportInputData = ReportInputData(area = 50, rooms = 2, city = WRO)
        val template = SimpleTextReportTemplate()
        val emailMetadata = EmailMetadata(to = "customer@company.com", subject = "Property price report")

        //when
        propertyPriceReportOrchestrator.generatePricesReportAndSend(template, reportInputData, emailMetadata)

        //then
        assertThat(emailSenderFake.numOfEmailsSent == 1)
        assertThat(emailSenderFake.lastEmail != null)
        assertThat(emailSenderFake.lastEmail == Email(
            metadata = EmailMetadata("customer@company.com", "Property price report"),
            content = "This is report")
        )
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

    private val propertyPriceCalculatorDummy = object : PropertyPriceCalculator {
        override fun price(area: Int, rooms: Int, city: City): Int {
            return 11800
        }
    }

    private val propertyPriceReportTemplatesStub = object : PropertyPriceReportTemplates {
        override fun generatePricesReport(template: ReportTemplate, reportInputData: ReportInputData): Report {
            return Report("This is report")
        }
    }

    private val emailSenderFake = object : EmailSender {
        var numOfEmailsSent = 0
        var lastEmail: Email? = null
        override fun sendEmail(to: String, subject: String, body: String) {
            numOfEmailsSent += 1
            lastEmail = Email(
                metadata = EmailMetadata(to, subject),
                content = body
            )
        }
    }

}

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