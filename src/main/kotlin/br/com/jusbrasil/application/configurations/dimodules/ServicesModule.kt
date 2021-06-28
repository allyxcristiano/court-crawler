package br.com.jusbrasil.application.configurations.dimodules

import br.com.jusbrasil.application.configurations.dimodules.CrawlerModule.COURT_OF_AL_FIRST_DEGREE_CRAWLER
import br.com.jusbrasil.application.configurations.dimodules.CrawlerModule.COURT_OF_AL_SECOND_DEGREE_CRAWLER
import br.com.jusbrasil.application.configurations.dimodules.CrawlerModule.COURT_OF_MS_FIRST_DEGREE_CRAWLER
import br.com.jusbrasil.application.configurations.dimodules.CrawlerModule.COURT_OF_MS_SECOND_DEGREE_CRAWLER
import br.com.jusbrasil.domain.processimport.services.ProcessService
import br.com.jusbrasil.domain.processimport.services.implementations.ProcessServiceImpl
import br.com.jusbrasil.domain.processimport.vos.enums.CourtOfJustice
import org.koin.dsl.module.module

object ServicesModule {
    fun modules() = module {
        single<ProcessService> {
            ProcessServiceImpl(
                get(),
                get(),
                mapOf(
                    CourtOfJustice.COURT_OF_JUSTICE_OF_AL to get(COURT_OF_AL_FIRST_DEGREE_CRAWLER),
                    CourtOfJustice.COURT_OF_JUSTICE_OF_MS to get(COURT_OF_MS_FIRST_DEGREE_CRAWLER)
                ),
                mapOf(
                    CourtOfJustice.COURT_OF_JUSTICE_OF_AL to get(COURT_OF_AL_SECOND_DEGREE_CRAWLER),
                    CourtOfJustice.COURT_OF_JUSTICE_OF_MS to get(COURT_OF_MS_SECOND_DEGREE_CRAWLER)
                )
            )
        }
    }
}

