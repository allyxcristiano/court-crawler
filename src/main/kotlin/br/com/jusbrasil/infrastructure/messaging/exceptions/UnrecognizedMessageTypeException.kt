package br.com.jusbrasil.infrastructure.messaging.exceptions

class UnrecognizedMessageTypeException(
    override val message: String?
) : Exception()
