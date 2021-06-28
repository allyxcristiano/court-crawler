package br.com.jusbrasil.componentTest.components.jms

import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.Connection
import javax.jms.Session

object ActiveMQJmsFactory {

    fun createConnection() = ActiveMQConnectionFactory(
        "vm://localhost?broker.persistent=false"
    ).createConnection()!!

    fun createSession(connection: Connection) = connection.createSession(
        false,
        Session.CLIENT_ACKNOWLEDGE
    )!!
}
