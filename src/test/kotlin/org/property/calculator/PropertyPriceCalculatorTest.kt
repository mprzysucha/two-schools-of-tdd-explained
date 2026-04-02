package org.property.calculator

import org.property.calculator.City.Companion.WRO
import org.property.tooling.Assert.assertThat
import org.property.tooling.Failure
import org.property.tooling.OneArgSpy
import org.property.tooling.Try

class PropertyPriceCalculatorTest {

    fun testWithStub() {
        //given
        val marketPricesProviderStub = object : MarketPricesProvider {
            override fun providePrice(city: City): Int = when(city) {
                WRO -> 13200
                else -> 11800
            }
        }
        val propertyPriceCalculator = RealPropertyPriceCalculator(marketPricesProviderStub)

        //when
        val actualValue = propertyPriceCalculator.price(area = 60, rooms = 2, city = WRO)

        //then
        assertThat(actualValue == 792000)
    }

    fun testWithDummy() {
        //given
        val marketPricesProviderDummy = object : MarketPricesProvider {
            override fun providePrice(city: City): Int = TODO("Not implemented")
        }
        val propertyPriceCalculator = RealPropertyPriceCalculator(marketPricesProviderDummy)

        //when
        val result = Try { propertyPriceCalculator.price(area = -100, rooms = 2, city = WRO) }

        //then
        assertThat(result is Failure)
        assertThat((result as Failure).e is NonPositiveNumber)
        assertThat(result.e.message == "Flat area")
    }

    fun testWithSpy() {
        //given
        val marketPricesProviderSpy = object : MarketPricesProvider, OneArgSpy<City>() {
            private val realObject = RealMarketPricesProvider()
            override fun providePrice(city: City): Int {
                super.storeArg(city)
                return realObject.providePrice(city)
            }
        }
        val propertyPriceCalculator = RealPropertyPriceCalculator(marketPricesProviderSpy)

        //when
        propertyPriceCalculator.price(area = 50, rooms = 2, city = WRO)

        //then
        assertThat(marketPricesProviderSpy.methodWasCalled(numOfTimes = 1))
        assertThat(marketPricesProviderSpy.capturedArgument() == WRO)
    }

}
