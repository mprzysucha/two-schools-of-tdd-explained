package org.property.tooling

sealed class Try {
    companion object {
        operator fun <T> invoke(block: () -> T): Try {
            return try {
                Success(block())
            } catch (e: Exception) {
                Failure(e)
            }
        }
    }
}

data class Failure(val e: Exception) : Try()
data class Success<T>(val res: T) : Try()
