package org.property.tooling

import kotlin.reflect.KFunction0

object SuiteRunner {

    fun runTests(tests: List<KFunction0<Unit>>, successMessage: () -> Any = { "SUCCESS" }) {
        for (test in tests) {
            test().also {
                println("${test.name} ${successMessage()}")
            }
        }
    }
}