package br.com.jusbrasil.application.processimport.producers

import br.com.jusbrasil.application.processimport.dtos.request.ProcessImportRequest
import br.com.jusbrasil.infrastructure.messaging.jms.JmsFactory
import br.com.jusbrasil.infrastructure.messaging.jms.JmsProducer
import org.koin.core.Koin.Companion.logger
import javax.jms.Session

class ProcessImportProducer(
    session: Session,
    queueName: String
) : JmsProducer(
    session,
    JmsFactory.createProducer(queueName, session)
) {
    fun produceMessage(request: ProcessImportRequest) {
        logger.info("Producing message to import process number ${request.processNumber}")
        produce(request)
        logger.info("Message to import process number ${request.processNumber} sent")
    }
}
