package org.property.calculator

interface PropertyPriceCalculator {
    fun price(area: Int, rooms: Int, city: City): Int
}

class RealPropertyPriceCalculator(val marketPricesProvider: MarketPricesProvider) : PropertyPriceCalculator {
    override fun price(area: Int, rooms: Int, city: City): Int {
        if (area <= 0) throw NonPositiveNumber("Flat area")
        if (rooms <= 0) throw NonPositiveNumber("Number of rooms")
        return (marketPricesProvider.providePrice(city) * area * multiplier(rooms)).toInt()
    }

    private fun multiplier(rooms: Int): Double {
        return when(rooms) {
            1 -> 1.09
            2 -> 1.0
            3 -> 0.93
            4 -> 0.87
            else -> 0.83
        }
    }
}

class NonPositiveNumber(msg: String) : RuntimeException(msg)
