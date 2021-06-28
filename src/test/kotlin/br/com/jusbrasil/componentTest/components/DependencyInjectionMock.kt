package br.com.jusbrasil.componentTest.components

import br.com.jusbrasil.application.configurations.dimodules.DatasourceModule
import br.com.jusbrasil.application.configurations.dimodules.MessagingModule
import br.com.jusbrasil.infrastructure.datasources.DataSource
import com.mongodb.client.MongoDatabase
import io.mockk.every
import io.mockk.mockkObject
import org.koin.dsl.module.module

object DependencyInjectionMock {

    private fun databaseModuleMock(datasource: DataSource<MongoDatabase>) = module {
        single { datasource }
    }

    fun mockDatasourceModule(mock: DataSource<MongoDatabase>) {
        mockkObject(DatasourceModule)
        every { DatasourceModule.modules() } returns databaseModuleMock(mock)
    }

    fun mockQueues() {
        mockkObject(MessagingModule)
        every { MessagingModule.modules() } returns QueueModuleMock.queueModuleMock
    }
}
