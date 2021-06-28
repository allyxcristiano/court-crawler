package br.com.jusbrasil.infrastructure.datasources.implementations

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import br.com.jusbrasil.application.configurations.ObjectMapperBuilder
import br.com.jusbrasil.infrastructure.datasources.DataSource
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.litote.kmongo.util.KMongoConfiguration

class MongoDatasourceImpl(
    private val environmentConfiguration: EnvironmentConfiguration
) : DataSource<MongoDatabase> {

    private val mongoDatabase: MongoDatabase

    init {
        val mapper = ObjectMapperBuilder.getMapper()
        KMongoConfiguration.bsonMapper.propertyNamingStrategy = mapper.propertyNamingStrategy
        KMongoConfiguration.bsonMapperCopy.propertyNamingStrategy = mapper.propertyNamingStrategy

        mongoDatabase = KMongo.createClient(
            getServerAddresses(),
            getMongoCredentials()
        ).getDatabase(environmentConfiguration.mongoDatabase)
    }

    override fun getDatabase(): MongoDatabase = mongoDatabase

    private fun getServerAddresses() = environmentConfiguration.mongoHosts.mapIndexed { index, it ->
        ServerAddress(it, getPort(index))
    }

    private fun getPort(index: Int) = if (index < environmentConfiguration.mongoPorts.size) {
        environmentConfiguration.mongoPorts[index].toInt()
    } else {
        environmentConfiguration.mongoPorts.first().toInt()
    }

    private fun getMongoCredentials() =
        listOf(
            MongoCredential.createCredential(
                environmentConfiguration.mongoUsername,
                environmentConfiguration.mongoDatabase,
                environmentConfiguration.mongoPassword.toCharArray()
            )
        )
}
