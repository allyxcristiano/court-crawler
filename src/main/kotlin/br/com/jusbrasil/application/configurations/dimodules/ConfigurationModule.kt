package br.com.jusbrasil.application.configurations.dimodules

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import br.com.jusbrasil.application.configurations.ObjectMapperBuilder
import br.com.jusbrasil.infrastructure.crawler.CrawlDriver
import org.koin.dsl.module.module

object ConfigurationModule {
    fun modules() = module {
        single { EnvironmentConfiguration() }
        single { ObjectMapperBuilder.getMapper() }
        single(createOnStart = true) { CrawlDriver.driver(get()) }
    }
}
