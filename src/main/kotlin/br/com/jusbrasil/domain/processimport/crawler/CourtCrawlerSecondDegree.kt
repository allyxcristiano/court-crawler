package br.com.jusbrasil.domain.processimport.crawler

import br.com.jusbrasil.domain.exceptions.ElementNotFoundException
import br.com.jusbrasil.domain.exceptions.RelatedProcessException
import br.com.jusbrasil.domain.processimport.vos.HistoryOfDegree
import br.com.jusbrasil.domain.processimport.vos.SecondDegree
import br.com.jusbrasil.domain.processimport.vos.enums.CourtOfJustice
import br.com.jusbrasil.infrastructure.Regex
import org.koin.core.Koin.Companion.logger
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.remote.RemoteWebDriver
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

abstract class CourtCrawlerSecondDegree(
    private val driver: RemoteWebDriver
) {
    private lateinit var processData: List<String>

    fun crawlSecondDegree(processNumber: String): SecondDegree? {
        var secondDegree: SecondDegree? = null

        val timeToProcess = measureTimeMillis {

            accessPageUrl()
            fillFirstPartOfProcess(processNumber)
            fillSecondPartOfProcess(processNumber)
            clickOnButtonToSearchProcess()

            if (processContainsOnlyRelatedProcess()) {
                throw RelatedProcessException()
            }

            if (processExists()) {
                clickOnButtonToExpandProcessHistory()
                clickOnButtonToExpandParts()

                processData = driver.findElementByXPath(pathOfProcessData()).text.split("\n")

                secondDegree = SecondDegree(
                    crawlGrade(),
                    crawlArea(),
                    crawlSubject(),
                    crawlPartsOfProcess(),
                    crawlHistoryOfDegree()
                )
            }
        }

        logger.info(
            "Process number $processNumber of court ${courtOfJustice()} and second degree crawled in $timeToProcess ms"
        )

        return secondDegree
    }

    private fun accessPageUrl() {
        driver.get(urlOfPage())
    }

    private fun fillFirstPartOfProcess(processNumber: String) {
        driver.findElementByXPath(pathOfFirstPartOfProcess()).sendKeys(processNumber.firstPartOfProcess())
    }

    private fun fillSecondPartOfProcess(processNumber: String) {
        driver.findElementByXPath(pathOfSecondPartOfProcess()).sendKeys(processNumber.secondPartOfProcess())
    }

    private fun clickOnButtonToSearchProcess() {
        driver.findElementByXPath(pathOfButtonToSearchProcess()).click()
    }

    private fun processContainsOnlyRelatedProcess() =
        driver.findElementsByClassName(pathOfLinkToRelatedProcess()).isNotEmpty()

    private fun processExists() = try {
        driver.findElementByXPath(pathOfLabelProcessNotFound())
        false
    } catch (e: NoSuchElementException) {
        true
    }

    private fun clickOnButtonToExpandParts() {
        try {
            driver.findElementById(idOfLinkToExpandParts())
            (driver as JavascriptExecutor).executeScript(
                "document.getElementById('${idOfLinkToExpandParts()}').click();"
            )
        } catch (e: NoSuchElementException) {
            logger.info("Process has no parts to expand")
        }
    }

    private fun clickOnButtonToExpandProcessHistory() {
        try {
            driver.findElementById(idOfLinkToExpandProcessHistory())
            (driver as JavascriptExecutor).executeScript(
                "document.getElementById('${idOfLinkToExpandProcessHistory()}').click();"
            )
        } catch (e: NoSuchElementException) {
            logger.info("Process has no more than 5 movement histories")
        }
    }

    private fun crawlGrade(): String {
        processData.forEachIndexed { index, value ->
            if (value == "Classe:")
                return processData[index + 1]
        }

        throw ElementNotFoundException("grade")
    }

    private fun crawlArea(): String {
        processData.forEachIndexed { index, value ->
            if (value == "Área :")
                return processData[index + 1]
        }

        throw ElementNotFoundException("area")
    }

    private fun crawlSubject(): String? {
        processData.forEachIndexed { index, value ->
            if (value.contains("Assunto:"))
                return processData[index + 1]
        }

        return null
    }

    private fun crawlPartsOfProcess(): List<String> {
        return driver.findElementByXPath(pathOfParts()).text.split("\n")
    }

    private fun crawlHistoryOfDegree(): List<HistoryOfDegree> {
        val listOfHistoryToTreat =
            driver.findElementByXPath(pathOfHistoryOfDegree()).text.split("\n").toMutableList()

        val historyOfDegree = mutableListOf<HistoryOfDegree>()
        val addHistoryWhenDateAndDescriptionAreNotNull: (LocalDate?, String?) -> Unit = { a, b ->
            if (a != null && b != null) {
                historyOfDegree.add(
                    HistoryOfDegree(a, b)
                )
            }
        }

        var date: LocalDate? = null
        var description: String? = null

        while (listOfHistoryToTreat.isNotEmpty()) {
            val lineToRead = listOfHistoryToTreat.first()
            listOfHistoryToTreat.removeProcessedLine()
            val partThatMayContainDate = lineToRead.take(10)

            if (partThatMayContainDate.matches(Regex.dateRegexWithDayMonthAndYear())) {
                addHistoryWhenDateAndDescriptionAreNotNull(date, description)
                date = formattedDate(partThatMayContainDate)
                description = lineToRead.removePrefix(partThatMayContainDate)
            } else {
                description += " $lineToRead"
            }
        }

        addHistoryWhenDateAndDescriptionAreNotNull(date, description)

        return historyOfDegree
    }

    private fun formattedDate(date: CharSequence): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.parse(date, formatter)
    }

    protected abstract fun urlOfPage(): String

    protected abstract fun pathOfFirstPartOfProcess(): String

    protected abstract fun pathOfSecondPartOfProcess(): String

    protected abstract fun pathOfButtonToSearchProcess(): String

    protected abstract fun pathOfLinkToRelatedProcess(): String

    protected abstract fun pathOfLabelProcessNotFound(): String

    protected abstract fun idOfLinkToExpandParts(): String

    protected abstract fun idOfLinkToExpandProcessHistory(): String

    protected abstract fun pathOfProcessData(): String

    protected abstract fun pathOfParts(): String

    protected abstract fun pathOfHistoryOfDegree(): String

    protected abstract fun courtOfJustice(): CourtOfJustice

    private fun String.firstPartOfProcess() = this.subSequence(0, 13)

    private fun String.secondPartOfProcess() = this.subSequence(16, 20)

    private fun MutableList<String>.removeProcessedLine() = this.removeAt(0)
}
