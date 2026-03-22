package org.property.reports

import org.property.calculator.EmailSender
import org.property.calculator.PropertyPriceCalculator

open class PropertyPriceReportOrchestrator(val propertyPriceCalculator: PropertyPriceCalculator, val propertyPriceReportTemplates: PropertyPriceReportTemplates, val emailSender: EmailSender) {

    fun generatePricesReport(template: ReportTemplate, reportInputData: ReportInputData, email: String, subject: String) {
        val price = propertyPriceCalculator.price(reportInputData.area, reportInputData.rooms, reportInputData.city)
        val report = propertyPriceReportTemplates.generatePricesReport(template, reportInputData.copy(price = price))
        emailSender.sendEmail(email, subject, report.content)
    }

}