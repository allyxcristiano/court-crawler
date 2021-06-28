package br.com.jusbrasil.infrastructure.messaging.jms

import br.com.jusbrasil.application.configurations.ObjectMapperBuilder
import org.koin.core.Koin.Companion.logger
import javax.jms.MessageProducer
import javax.jms.Session

abstract class JmsProducer(
    private val session: Session,
    private val producer: MessageProducer
) {
    protected fun produce(sendObject: Any) {
        logger.debug("Sending message to queue")

        synchronized(this) {
            producer.send(
                session.createTextMessage(
                    ObjectMapperBuilder.getMapper().writeValueAsString(sendObject)
                )
            )
        }

        logger.debug("Message sent to queue")
    }

    protected fun produce(txt: String) {
        logger.debug("Sending message to queue")

        synchronized(this) {
            producer.send(
                session.createTextMessage(txt)
            )
        }

        logger.debug("Message sent to queue")
    }
}
