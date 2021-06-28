package br.com.jusbrasil.application.configurations.dimodules

import br.com.jusbrasil.domain.processimport.crawler.CourtCrawlerFirstDegree
import br.com.jusbrasil.domain.processimport.crawler.CourtCrawlerSecondDegree
import br.com.jusbrasil.domain.processimport.crawler.implementations.CourtOfALFirstDegreeCrawler
import br.com.jusbrasil.domain.processimport.crawler.implementations.CourtOfALSecondDegreeCrawler
import br.com.jusbrasil.domain.processimport.crawler.implementations.CourtOfMSFirstDegreeCrawler
import br.com.jusbrasil.domain.processimport.crawler.implementations.CourtOfMSSecondDegreeCrawler
import org.koin.dsl.module.module

object CrawlerModule {

    const val COURT_OF_AL_FIRST_DEGREE_CRAWLER = "COURT_OF_AL_FIRST_DEGREE_CRAWLER"
    const val COURT_OF_AL_SECOND_DEGREE_CRAWLER = "COURT_OF_AL_SECOND_DEGREE_CRAWLER"
    const val COURT_OF_MS_FIRST_DEGREE_CRAWLER = "COURT_OF_MS_FIRST_DEGREE_CRAWLER"
    const val COURT_OF_MS_SECOND_DEGREE_CRAWLER = "COURT_OF_MS_SECOND_DEGREE_CRAWLER"

    fun modules() = module {
        single<CourtCrawlerFirstDegree>(COURT_OF_AL_FIRST_DEGREE_CRAWLER) {
            CourtOfALFirstDegreeCrawler(
                get(),
                get()
            )
        }
        single<CourtCrawlerSecondDegree>(COURT_OF_AL_SECOND_DEGREE_CRAWLER) {
            CourtOfALSecondDegreeCrawler(
                get(),
                get()
            )
        }
        single<CourtCrawlerFirstDegree>(COURT_OF_MS_FIRST_DEGREE_CRAWLER) {
            CourtOfMSFirstDegreeCrawler(
                get(),
                get()
            )
        }
        single<CourtCrawlerSecondDegree>(COURT_OF_MS_SECOND_DEGREE_CRAWLER) {
            CourtOfMSSecondDegreeCrawler(
                get(),
                get()
            )
        }
    }
}
