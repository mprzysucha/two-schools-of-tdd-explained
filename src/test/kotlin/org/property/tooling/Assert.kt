package org.property.tooling

object Assert {

    fun assert(value: Boolean) {
        assert(value) { "Assertion failed" }
    }

    fun assert(value: Boolean, lazyMessage: () -> Any) {
        if (!value) {
            val message = lazyMessage()
            throw AssertionError(message)
        }
    }

}