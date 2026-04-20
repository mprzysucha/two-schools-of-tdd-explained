package org.property.tooling

object Assert {

    fun <T> assertEquals(actual: T, expected: T) {
        assertThat(actual == expected, { "$actual not equal to $expected" })
    }

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