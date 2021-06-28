package br.com.jusbrasil.application.configurations.dimodules

import br.com.jusbrasil.application.processimport.web.routes.ProcessRoutes
import org.koin.dsl.module.module

object RoutesModule {
    fun modules() = module {
        single { ProcessRoutes(get()) }
    }
}
