package br.com.jusbrasil.infrastructure.messaging.jms

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnection
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQSClientBuilder

object AwsConnectionFactory : JmsConnectionFactory {

    override fun connection(environmentConfiguration: EnvironmentConfiguration): SQSConnection {
        return if (environmentConfiguration.awsEndpointUrl.isNotBlank()) {
            SQSConnectionFactory(
                ProviderConfiguration(),
                AmazonSQSClientBuilder
                    .standard()
                    .withEndpointConfiguration(
                        AwsClientBuilder.EndpointConfiguration(
                            environmentConfiguration.awsEndpointUrl,
                            environmentConfiguration.awsRegion
                        )
                    )
            ).createConnection()
        } else {
            SQSConnectionFactory(
                ProviderConfiguration(),
                AmazonSQSClientBuilder.defaultClient()
            ).createConnection()
        }
    }
}
