package org.property.calculator

import org.property.calculator.City.Companion.KAT
import org.property.calculator.City.Companion.SZC
import org.property.calculator.City.Companion.WRO
import org.property.tooling.Assert.assert
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
        assert(actualValue == 792000)
    }

    //Deprecated: see better example in PropertyPriceReportOrchestratorTest.testReports()
    fun testWithMock() {
        val marketPricesProviderMock = object : MarketPricesProvider {
            private var providePriceCounter = 0
            private var argumentsTimeline = mutableListOf<City>()
            fun providePriceWasCalled(numOfTimes: Int = 1) = providePriceCounter == numOfTimes
            fun capturedArgument(callNum: Int = 0) = argumentsTimeline.get(callNum)
            override fun providePrice(city: City): Int {
                providePriceCounter += 1
                argumentsTimeline.addFirst(city)
                return 11800
            }
        }
        val propertyPriceCalculator = RealPropertyPriceCalculator(marketPricesProviderMock)

        propertyPriceCalculator.price(area = 50, rooms = 2, city = WRO)

        assert(marketPricesProviderMock.providePriceWasCalled(numOfTimes = 1))
        assert(marketPricesProviderMock.capturedArgument() == WRO)
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
        assert(result is Failure)
        assert((result as Failure).e is NonPositiveNumber)
        assert(result.e.message == "Flat area")
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
        assert(marketPricesProviderSpy.methodWasCalled(numOfTimes = 1))
        assert(marketPricesProviderSpy.capturedArgument() == WRO)
    }

    private class FakeMarketPricesProvider : MarketPricesProvider {
        override fun providePrice(city: City): Int = when(city) {
            WRO -> 13200
            SZC -> 9900
            KAT -> 9400
            else -> 11800
        }
    }

    fun testWithFake() {
        //given
        val marketPricesProviderFake = FakeMarketPricesProvider()
        val propertyPriceCalculator = RealPropertyPriceCalculator(marketPricesProviderFake)

        //when
        val price = propertyPriceCalculator.price(area = 60, rooms = 2, city = WRO)

        //then
        assert(price == 792000)
    }

}
