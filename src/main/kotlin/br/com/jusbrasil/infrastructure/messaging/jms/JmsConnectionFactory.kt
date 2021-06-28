package br.com.jusbrasil.infrastructure.messaging.jms

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import javax.jms.Connection

interface JmsConnectionFactory {

    fun connection(environmentConfiguration: EnvironmentConfiguration): Connection
}
