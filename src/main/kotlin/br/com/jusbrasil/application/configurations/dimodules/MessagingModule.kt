package br.com.jusbrasil.application.configurations.dimodules

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import br.com.jusbrasil.application.processimport.listeners.ProcessImportListener
import br.com.jusbrasil.application.processimport.producers.ProcessImportProducer
import br.com.jusbrasil.infrastructure.messaging.jms.JmsFactory
import org.koin.dsl.module.module

object MessagingModule {
    fun modules() = module {
        single {
            JmsFactory.connection(get())
        }
        single(createOnStart = true) {
            ProcessImportListener(
                get(),
                get(),
                get<EnvironmentConfiguration>().processImportQueueName
            )
        }
        single() {
            ProcessImportProducer(
                JmsFactory.session(get()), get<EnvironmentConfiguration>().processImportQueueName
            )
        }
    }
}
