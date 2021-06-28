package br.com.jusbrasil.domain.processimport.services.implementations

import br.com.jusbrasil.application.processimport.dtos.request.ProcessImportRequest
import br.com.jusbrasil.application.processimport.producers.ProcessImportProducer
import br.com.jusbrasil.domain.exceptions.ImportFailException
import br.com.jusbrasil.domain.exceptions.InvalidProcessNumberException
import br.com.jusbrasil.domain.exceptions.ResourceAlreadyExistsException
import br.com.jusbrasil.domain.processimport.crawler.CourtCrawlerFirstDegree
import br.com.jusbrasil.domain.processimport.crawler.CourtCrawlerSecondDegree
import br.com.jusbrasil.domain.processimport.repositories.ProcessRepository
import br.com.jusbrasil.domain.processimport.services.ProcessService
import br.com.jusbrasil.domain.processimport.vos.Process
import br.com.jusbrasil.domain.processimport.vos.enums.CourtOfJustice

class ProcessServiceImpl(
    private val processImportProducer: ProcessImportProducer,
    private val processRepository: ProcessRepository,
    private val crawlersOfFirstDegreeByCourt: Map<CourtOfJustice, CourtCrawlerFirstDegree>,
    private val crawlersOfSecondDegreeByCourt: Map<CourtOfJustice, CourtCrawlerSecondDegree>
) : ProcessService {

    override fun produceMessageToImportProcess(request: ProcessImportRequest) {
        val processNumberWithOnlyNumbers = request.processNumber.processNumberWithOnlyNumbers()

        require(processNumberWithOnlyNumbers.length == 20) {
            throw InvalidProcessNumberException()
        }

        processImportProducer.produceMessage(request)
    }

    override fun importProcess(request: ProcessImportRequest) {
        val processNumberWithOnlyNumbers = request.processNumber.processNumberWithOnlyNumbers()
        val courtOfJustice = processNumberWithOnlyNumbers.courtOfJustice()

        if (processRepository.processExists(processNumberWithOnlyNumbers)) {
            throw ResourceAlreadyExistsException()
        }

        val firstDegree =
            crawlersOfFirstDegreeByCourt[courtOfJustice]?.crawlFirstDegree(processNumberWithOnlyNumbers)

        require(firstDegree != null) {
            throw ImportFailException()
        }

        val secondDegree =
            crawlersOfSecondDegreeByCourt[courtOfJustice]?.crawlSecondDegree(processNumberWithOnlyNumbers)

        val process = Process(
            processNumberWithOnlyNumbers,
            courtOfJustice,
            firstDegree,
            secondDegree
        )

        processRepository.save(process)
    }

    override fun retrieveProcess(processNumber: String): Process? {
        val processNumberTreated = processNumber.processNumberWithOnlyNumbers()

        require(processNumberTreated.length == 20) {
            throw InvalidProcessNumberException()
        }

        return processRepository.retrieve(processNumberTreated)
    }

    override fun retrieve(): List<Process> {
        return processRepository.retrieve()
    }

    private fun String.courtOfJustice() = CourtOfJustice.valueOrDomainException(this.substring(14, 16))

    private fun String.processNumberWithOnlyNumbers() = replace("-", "").replace(".", "")
}
