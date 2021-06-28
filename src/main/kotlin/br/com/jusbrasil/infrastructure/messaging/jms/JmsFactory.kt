package br.com.jusbrasil.infrastructure.messaging.jms

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import com.amazon.sqs.javamessaging.SQSSession
import org.koin.core.Koin.Companion.logger
import javax.jms.Connection
import javax.jms.Session

object JmsFactory {
    @SuppressWarnings("TooGenericExceptionCaught")
    fun createConsumer(queueAddress: String, session: Session) = try {
        session.createQueue(
            queueAddress
        ).let { queue ->
            session.createConsumer(queue)
        }
    } catch (e: Exception) {
        logger.err("Consumer for created messages could not be initialized")
        throw e
    }!!

    @SuppressWarnings("TooGenericExceptionCaught")
    fun createProducer(queueAddress: String, session: Session) = try {
        session.createQueue(
            queueAddress
        ).let { queue ->
            session.createProducer(queue)
        }
    } catch (e: Exception) {
        logger.err("Producer for created messages could not be initialized")
        throw e
    }!!

    fun connection(
        environmentConfiguration: EnvironmentConfiguration
    ): Connection = AwsConnectionFactory.connection(environmentConfiguration)

    fun session(
        connection: Connection
    ) = connection
        .createSession(false, SQSSession.UNORDERED_ACKNOWLEDGE)
}
