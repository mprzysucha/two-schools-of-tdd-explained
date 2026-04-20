package org.property.reports

import org.property.calculator.City

interface ReportTemplate {
    fun generate(reportInputData: ReportInputData): Report
}

class SimpleTextReportTemplate : ReportTemplate {
    override fun generate(reportInputData: ReportInputData): Report {
        return Report("The property in ${reportInputData.city.city} with ${reportInputData.rooms} rooms having ${reportInputData.area} m2 costs ${reportInputData.price}.")
    }
}

data class ReportInputData(
    val area: Int,
    val rooms: Int,
    val city: City,
    val price: Int? = null
)