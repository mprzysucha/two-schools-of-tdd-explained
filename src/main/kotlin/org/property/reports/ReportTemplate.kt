package org.property.reports

import org.property.calculator.City

class ReportTemplate

data class ReportInputData(
    val area: Int,
    val rooms: Int,
    val city: City,
    val price: Int? = null
)