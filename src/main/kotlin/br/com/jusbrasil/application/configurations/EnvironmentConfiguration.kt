package br.com.jusbrasil.application.configurations

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.getValue
import com.natpryce.konfig.intType
import com.natpryce.konfig.stringType

class EnvironmentConfiguration(
    configuration: Configuration = EnvironmentVariables()
) {
    val serverPort = configuration[SERVER_PORT]

    val processImportQueueName = configuration[PROCESS_IMPORT_QUEUE_NAME]

    val awsEndpointUrl = configuration.getOrElse(AWS_ENDPOINT_URL, "")
    val awsRegion = configuration.getOrElse(AWS_REGION, "")

    val mongoHosts = configuration[MONGO_HOSTS].split(",")
    val mongoPorts = configuration[MONGO_PORTS].split(",")
    val mongoUsername = configuration[MONGO_USERNAME]
    val mongoPassword = configuration[MONGO_PASSWORD]
    val mongoDatabase = configuration[MONGO_DATABASE]

    val courtOfALUrlFirstDegree = configuration[COURT_OF_AL_URL_FIRST_DEGREE]
    val courtOfALUrlSecondDegree = configuration[COURT_OF_AL_URL_SECOND_DEGREE]
    val courtOfMSUrlFirstDegree = configuration[COURT_OF_MS_URL_FIRST_DEGREE]
    val courtOfMSUrlSecondDegree = configuration[COURT_OF_MS_URL_SECOND_DEGREE]

    val chromeWebDriverLocation = configuration[CHROME_WEB_DRIVER_LOCATION]

    companion object {
        val SERVER_PORT by intType

        val PROCESS_IMPORT_QUEUE_NAME by stringType

        val AWS_ENDPOINT_URL by stringType
        val AWS_REGION by stringType

        val MONGO_HOSTS by stringType
        val MONGO_PORTS by stringType
        val MONGO_USERNAME by stringType
        val MONGO_PASSWORD by stringType
        val MONGO_DATABASE by stringType

        val COURT_OF_AL_URL_FIRST_DEGREE by stringType
        val COURT_OF_AL_URL_SECOND_DEGREE by stringType

        val COURT_OF_MS_URL_FIRST_DEGREE by stringType
        val COURT_OF_MS_URL_SECOND_DEGREE by stringType

        val CHROME_WEB_DRIVER_LOCATION by stringType
    }
}
