package org.property.reports

import org.property.calculator.EmailSender
import org.property.calculator.PropertyPriceCalculator
import org.property.email.EmailData

open class PropertyPriceReportOrchestrator(val propertyPriceCalculator: PropertyPriceCalculator, val propertyPriceReportTemplates: PropertyPriceReportTemplates, val emailSender: EmailSender) {

    fun generatePricesReportAndSend(template: ReportTemplate, reportInputData: ReportInputData, emailData: EmailData) {
        val price = propertyPriceCalculator.price(reportInputData.area, reportInputData.rooms, reportInputData.city)
        val report = propertyPriceReportTemplates.generatePricesReport(template, reportInputData.copy(price = price))
        emailSender.sendEmail(emailData.to, emailData.subject, report.content)
    }

}