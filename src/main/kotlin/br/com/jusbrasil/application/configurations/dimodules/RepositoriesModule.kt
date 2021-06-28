package br.com.jusbrasil.application.configurations.dimodules

import br.com.jusbrasil.domain.processimport.repositories.ProcessRepository
import br.com.jusbrasil.domain.processimport.repositories.implementations.ProcessRepositoryImpl
import org.koin.dsl.module.module

object RepositoriesModule {
    fun modules() = module {
        single<ProcessRepository> { ProcessRepositoryImpl(get()) }
    }
}
