package org.property.reports

interface PropertyPriceReportTemplates {
    fun generatePricesReport(template: ReportTemplate, reportInputData: ReportInputData): Report {
        return template.generate(reportInputData)
    }
}
