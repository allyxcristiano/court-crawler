package br.com.jusbrasil.application.configurations.dimodules

import br.com.jusbrasil.infrastructure.datasources.DataSource
import br.com.jusbrasil.infrastructure.datasources.implementations.MongoDatasourceImpl
import com.mongodb.client.MongoDatabase
import org.koin.dsl.module.module

object DatasourceModule {

    fun modules() = module {
        single<DataSource<MongoDatabase>> { MongoDatasourceImpl(get()) }
    }
}
