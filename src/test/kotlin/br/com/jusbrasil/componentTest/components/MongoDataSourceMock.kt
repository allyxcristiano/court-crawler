package br.com.jusbrasil.componentTest.components

import br.com.jusbrasil.application.configurations.ObjectMapperBuilder
import br.com.jusbrasil.infrastructure.datasources.DataSource
import com.mongodb.MongoClientOptions
import com.mongodb.MongoWriteException
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import org.bson.Document
import org.litote.kmongo.KMongo
import org.litote.kmongo.util.KMongoConfiguration

class MongoDataSourceMock(private val config: MongoConfig) : DataSource<MongoDatabase> {

    init {
        val mapper = ObjectMapperBuilder.getMapper()
        KMongoConfiguration.bsonMapper.propertyNamingStrategy = mapper.propertyNamingStrategy
        KMongoConfiguration.bsonMapperCopy.propertyNamingStrategy = mapper.propertyNamingStrategy
    }

    override fun getDatabase() = database

    data class MongoConfig(
        val host: String = "localhost",
        val port: Int = 27018,
        val databaseName: String = "admin"
    )

    var isStarted = false
    private lateinit var mongoDaemon: MongodProcess
    private lateinit var database: MongoDatabase

    fun start() {
        database = loadDatabase()
        mongoDaemon = startDeamon()
        isStarted = true
    }

    fun clear() {
        database.listCollectionNames().forEach {
            val collection = database.getCollection(it)
            try {
                collection.deleteMany(Document.parse("{}"))
            } catch (ex: MongoWriteException) {
                /** bypass errors raised when try to delete system documents */
            }
        }
    }

    fun destroy() {
        if (isStarted) mongoDaemon.stop()
    }

    /**
     * Creates an implementation of [MongoDataSourceMock] to be used in component tests
     */
    fun getMockedDataSource() = object : DataSource<MongoDatabase> {
        override fun getDatabase(): MongoDatabase = database
    }

    fun getCollection(collectionName: String) =
        getMockedDataSource().getDatabase().getCollection(collectionName)

    private fun loadDatabase() = KMongo.createClient(
        listOf(
            ServerAddress(
                config.host,
                config.port
            )
        ),
        MongoClientOptions
            .builder()
            .maxConnectionIdleTime(600000)
            .build()
    ).getDatabase(config.databaseName)


    private fun startDeamon(): MongodProcess {
        return MongodStarter.getDefaultInstance().prepare(
            MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(
                    Net(
                        config.host,
                        config.port,
                        Network.localhostIsIPv6()
                    )
                )
                .build()
        ).start()
    }
}
