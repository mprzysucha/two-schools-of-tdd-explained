package org.property.tooling

open class OneArgSpy<T> {
    private var methodCallCounter = 0
    private var argumentsTimeline = mutableListOf<T>()
    fun methodWasCalled(numOfTimes: Int = 1) = methodCallCounter == numOfTimes
    fun capturedArgument(callNum: Int = 0) = argumentsTimeline.get(callNum)
    fun storeArg(arg: T) {
        methodCallCounter += 1
        argumentsTimeline.addFirst(arg)
    }
}