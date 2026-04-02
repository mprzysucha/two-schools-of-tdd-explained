package org.property.tooling

object Assert {

    fun assertThat(value: Boolean) {
        assertThat(value, { "Assertion failed" })
    }

    fun assertThat(value: Boolean, lazyMessage: () -> Any) {
        if (!value) {
            val message = lazyMessage()
            throw AssertionError(message)
        }
    }

}