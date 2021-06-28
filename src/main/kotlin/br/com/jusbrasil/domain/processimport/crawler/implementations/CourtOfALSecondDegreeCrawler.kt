package br.com.jusbrasil.domain.processimport.crawler.implementations

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import br.com.jusbrasil.domain.processimport.crawler.CourtCrawlerSecondDegree
import br.com.jusbrasil.domain.processimport.vos.enums.CourtOfJustice
import org.openqa.selenium.remote.RemoteWebDriver

class CourtOfALSecondDegreeCrawler(
    private val environmentConfiguration: EnvironmentConfiguration,
    private val driver: RemoteWebDriver
) : CourtCrawlerSecondDegree(driver) {

    override fun urlOfPage() = environmentConfiguration.courtOfALUrlSecondDegree

    override fun pathOfFirstPartOfProcess() = """//*[@id="numeroDigitoAnoUnificado"]"""

    override fun pathOfSecondPartOfProcess() = """//*[@id="foroNumeroUnificado"]"""

    override fun pathOfButtonToSearchProcess() = """//*[@id="botaoPesquisar"]"""

    override fun pathOfLinkToRelatedProcess() = "linkProcesso"

    override fun pathOfLabelProcessNotFound(): String {
        return "/html/body/table[4]/tbody/tr/td/form/div[1]/table[1]/tbody/tr[2]/td[2]/li"
    }

    override fun idOfLinkToExpandParts() = """linkpartes"""

    override fun idOfLinkToExpandProcessHistory() = """linkmovimentacoes"""

    override fun pathOfProcessData() = """/html/body/div[1]/table[4]/tbody/tr/td/div[1]/table[2]/tbody"""

    override fun pathOfParts(): String {
        return if (driver.findElementByXPath("""//*[@id="tablePartesPrincipais"]""").isDisplayed) {
            """//*[@id="tablePartesPrincipais"]"""
        } else {
            """//*[@id="tableTodasPartes"]"""
        }
    }

    override fun pathOfHistoryOfDegree(): String {
        return if (driver.findElementByXPath("""//*[@id="tabelaUltimasMovimentacoes"]""").isDisplayed) {
            """//*[@id="tabelaUltimasMovimentacoes"]"""
        } else {
            """//*[@id="tabelaTodasMovimentacoes"]"""
        }
    }

    override fun courtOfJustice() = CourtOfJustice.COURT_OF_JUSTICE_OF_AL
}
