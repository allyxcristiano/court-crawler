package br.com.jusbrasil.componentTest.commons

import br.com.jusbrasil.application.TJServiceApplication
import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import br.com.jusbrasil.componentTest.components.DependencyInjectionMock
import br.com.jusbrasil.componentTest.components.MongoDataSourceMock
import br.com.jusbrasil.componentTest.components.QueueModuleMock
import com.natpryce.konfig.Configuration
import com.natpryce.konfig.EnvironmentVariables
import io.javalin.Javalin
import io.mockk.unmockkAll
import org.koin.standalone.KoinComponent

class TestEnvironmentHelper(private val configuration: Configuration = EnvironmentVariables()) : KoinComponent {
    private val errors: MutableSet<Throwable> = mutableSetOf()
    private var javalinInstance: Javalin? = null
    var mongoDataSource: MongoDataSourceMock? = null

    fun mockDatasourceDependencyInjectionModule(): TestEnvironmentHelper {
        tryTo(false) {
            if (mongoDataSource != null && mongoDataSource!!.isStarted) {
                DependencyInjectionMock.mockDatasourceModule(mongoDataSource!!)
            } else {
                throw IllegalStateException("DataSource must be initialized")
            }
        }

        return this
    }

    fun provideAMongoInstance(): TestEnvironmentHelper {
        val database = tryToOrNull { configuration[EnvironmentConfiguration.MONGO_DATABASE] }
        val firstPort = tryToOrNull { configuration[EnvironmentConfiguration.MONGO_PORTS].split(",").first().toInt() }
        val firstHost = tryToOrNull { configuration[EnvironmentConfiguration.MONGO_HOSTS].split(",").first() }

        if (database != null && firstPort != null && firstHost != null) {
            mongoDataSource = tryToOrNull {
                MongoDataSourceMock(
                    MongoDataSourceMock.MongoConfig(
                        host = firstHost,
                        port = firstPort,
                        databaseName = database
                    )
                ).apply { start() }
            }
        }
        return this
    }

    fun setupAQueueInstance(): TestEnvironmentHelper {
        tryTo { DependencyInjectionMock.mockQueues() }
        return this
    }

    fun startApplication() {
        check(errors.isEmpty()) { "Was not possible to start the test environment - reason(s): $errors" }

        javalinInstance = TJServiceApplication().startApplication()
    }

    fun prepareTest() {
        clearMongoDatasource()
    }

    fun tearDown(unmockkAll: Boolean) {
        mongoDataSource?.destroy()
        javalinInstance?.stop()
        QueueModuleMock.processImportListener.stop()
        if (unmockkAll) unmockkAll()
    }

    private fun clearMongoDatasource() {
        checkNotNull(mongoDataSource) { "Mongo Datasource must be initialized" }
        mongoDataSource!!.clear()
    }

    private fun tryTo(bypassException: Boolean = false, execution: () -> (Unit)) {
        try {
            execution.invoke()
        } catch (e: Exception) {
            errors.add(e)
            if (!bypassException) throw e
        }
    }

    private fun <T> tryToOrNull(execution: () -> (T)): T? {
        return try {
            execution.invoke()
        } catch (e: Exception) {
            errors.add(e)
            null
        }
    }
}
