package org.property.email

data class EmailMetadata(
    val to: String,
    val subject: String
)

data class Email(
    val metadata: EmailMetadata,
    val content: String,
)
