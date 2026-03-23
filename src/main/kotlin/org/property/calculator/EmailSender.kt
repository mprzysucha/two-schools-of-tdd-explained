package org.property.calculator

interface EmailSender {
    fun sendEmail(to: String, subject: String, body: String)
}