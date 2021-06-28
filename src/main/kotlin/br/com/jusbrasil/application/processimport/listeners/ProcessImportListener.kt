package br.com.jusbrasil.application.processimport.listeners

import br.com.jusbrasil.application.processimport.dtos.request.ProcessImportRequest
import br.com.jusbrasil.domain.processimport.services.ProcessService
import br.com.jusbrasil.infrastructure.messaging.jms.JmsFactory
import br.com.jusbrasil.infrastructure.messaging.jms.JmsListener
import org.koin.core.Koin.Companion.logger
import javax.jms.Connection
import javax.jms.Message

class ProcessImportListener(
    private val processService: ProcessService,
    connection: Connection,
    queueName: String
) : JmsListener(
    connection,
    JmsFactory.createConsumer(queueName, JmsFactory.session(connection))
) {
    override fun onMessage(message: Message) = consume<ProcessImportRequest>(message) {
        logger.info("Consuming message to import process ${it.processNumber}")

        try {
            processService.importProcess(it)
            logger.info("Process $it imported")
        } catch (e: Exception) {
            logger.err("${e.message} | Process number ${it.processNumber}")
        }
    }

    public override fun stop() {
        super.stop()
    }
}
