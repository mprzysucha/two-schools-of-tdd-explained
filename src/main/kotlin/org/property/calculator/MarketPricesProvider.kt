package org.property.calculator

interface MarketPricesProvider {
    fun providePrice(city: City): Int
}

class RealMarketPricesProvider : MarketPricesProvider {
    override fun providePrice(city: City): Int {
        return PricesDatabaseFake.cityPrices[city] ?: PricesDatabaseFake.AVG_PRICE_IN_POLAND
    }
}

object PricesDatabaseFake {
    val cityPrices = mapOf(
        City.WRO to 13200,
        City.SZC to 9900,
        City.KAT to 9400,
    )
    const val AVG_PRICE_IN_POLAND = 11800
}

