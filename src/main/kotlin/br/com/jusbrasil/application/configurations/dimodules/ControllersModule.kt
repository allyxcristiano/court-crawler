package br.com.jusbrasil.application.configurations.dimodules

import br.com.jusbrasil.application.processimport.web.controllers.ProcessController
import org.koin.dsl.module.module

object ControllersModule {
    fun modules() = module {
        single { ProcessController(get()) }
    }
}
