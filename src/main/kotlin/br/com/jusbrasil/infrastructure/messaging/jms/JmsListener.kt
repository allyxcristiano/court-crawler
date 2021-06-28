package br.com.jusbrasil.infrastructure.messaging.jms

import br.com.jusbrasil.application.configurations.ObjectMapperBuilder
import br.com.jusbrasil.infrastructure.messaging.exceptions.UnrecognizedMessageTypeException
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.readValue
import org.koin.core.Koin.Companion.logger
import java.io.IOException
import javax.jms.Connection
import javax.jms.Message
import javax.jms.MessageConsumer
import javax.jms.MessageListener
import javax.jms.TextMessage

abstract class JmsListener(
    private val connection: Connection,
    private val consumer: MessageConsumer
) : MessageListener {

    init {
        logger.info("Starting ${this::class.java.simpleName}")
        consumer.messageListener = getListener()
        connection.start()
        logger.info("${this::class.java.simpleName} listening new requests")
    }

    inline fun <reified T> consume(message: Message, process: (extractedMessage: T) -> Unit) {
        try {
            logger.debug("Receiving new message from queue")

            val extractedMessage = extractMessage<T>(message)

            process(extractedMessage)

            message.acknowledge()
        } catch (
            @Suppress("TooGenericExceptionCaught")
            e: Exception
        ) {
            logger.err("Error receiving message from queue")
            throw e
        }
    }

    @SuppressWarnings("ThrowsCount")
    inline fun <reified T> extractMessage(message: Message): T {
        if (message !is TextMessage) {
            throw UnrecognizedMessageTypeException("The received message is not a TextMessage")
        }

        return try {
            ObjectMapperBuilder.getMapper().readValue(message.text)
        } catch (
            @Suppress("TooGenericExceptionCaught")
            e: Exception
        ) {
            when (e) {
                is IOException,
                is JsonParseException,
                is JsonMappingException ->
                    throw UnrecognizedMessageTypeException(
                        "Unrecognized message type. Was expecting ${T::class.java} but received ${message.text}"
                    )
                else -> throw e
            }
        }
    }

    protected open fun stop() {
        consumer.close()
        connection.close()
    }

    private fun getListener() = this
}
