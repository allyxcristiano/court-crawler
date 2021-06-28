package br.com.jusbrasil.componentTest.components

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import br.com.jusbrasil.application.processimport.listeners.ProcessImportListener
import br.com.jusbrasil.application.processimport.producers.ProcessImportProducer
import br.com.jusbrasil.componentTest.components.jms.ActiveMQJmsFactory
import br.com.jusbrasil.infrastructure.messaging.jms.JmsFactory
import io.mockk.every
import io.mockk.mockkObject
import org.koin.dsl.module.module

object QueueModuleMock {
    lateinit var processImportListener: ProcessImportListener

    val queueModuleMock = module {
        val environmentConfiguration = get<EnvironmentConfiguration>()

        val connection = ActiveMQJmsFactory.createConnection()
        single(createOnStart = true) { connection }

        mockkObject(JmsFactory)
        every { JmsFactory.session(any()) } returns ActiveMQJmsFactory.createSession(connection)

        factory { ActiveMQJmsFactory.createSession(get()) }

        single(createOnStart = true) {
            processImportListener = ProcessImportListener(get(), get(), environmentConfiguration.processImportQueueName)
        }

        single {
            ProcessImportProducer(get(), environmentConfiguration.processImportQueueName)
        }
    }
}
