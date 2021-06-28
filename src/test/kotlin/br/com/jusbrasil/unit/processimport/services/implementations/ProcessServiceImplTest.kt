package br.com.jusbrasil.unit.processimport.services.implementations

import br.com.jusbrasil.application.processimport.dtos.request.ProcessImportRequest
import br.com.jusbrasil.application.processimport.producers.ProcessImportProducer
import br.com.jusbrasil.domain.exceptions.ImportFailException
import br.com.jusbrasil.domain.exceptions.InvalidProcessNumberException
import br.com.jusbrasil.domain.exceptions.ResourceAlreadyExistsException
import br.com.jusbrasil.domain.processimport.crawler.CourtCrawlerFirstDegree
import br.com.jusbrasil.domain.processimport.crawler.CourtCrawlerSecondDegree
import br.com.jusbrasil.domain.processimport.repositories.ProcessRepository
import br.com.jusbrasil.domain.processimport.services.ProcessService
import br.com.jusbrasil.domain.processimport.services.implementations.ProcessServiceImpl
import br.com.jusbrasil.domain.processimport.vos.enums.CourtOfJustice
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals
import kotlin.test.assertFails

object ProcessServiceImplTest : Spek({

    Feature("Produce message to import process") {

        Scenario("When try to import process with invalid length should fail with InvalidProcessNumberException an expected message") {

            lateinit var processService: ProcessService
            lateinit var exception: Throwable

            Given("A valid process service") {
                processService = ProcessServiceImpl(
                    mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true)
                )
            }

            When("Try to import process with invalid length") {
                exception = assertFails {
                    processService.produceMessageToImportProcess(ProcessImportRequest("123456"))
                }
            }

            Then("Should fails with InvalidProcessNumberException") {
                assert(exception is InvalidProcessNumberException)
            }

            And("Expected message") {
                assertEquals("Process number size is invalid", exception.message)
            }
        }

        Scenario("When try to import process with invalid length should call the right components") {

            lateinit var processService: ProcessService
            lateinit var request: ProcessImportRequest
            lateinit var processImportProducer: ProcessImportProducer

            Given("A mocked ProcessImportProducer") {
                processImportProducer = mockk(relaxed = true)
            }

            And("A valid request") {
                request = ProcessImportRequest("0713663-77.2019.8.02.0001")
            }

            And("A ProcessImportProducer that runs without error") {
                every { processImportProducer.produceMessage(request) } just runs
            }

            Given("A valid process service") {
                processService = ProcessServiceImpl(
                    processImportProducer, mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true)
                )
            }

            When("Try to process with valid length") {
                processService.produceMessageToImportProcess(request)
            }

            Then("Should call the right components") {
                verifyOrder {
                    processImportProducer.produceMessage(request)
                }
            }
        }
    }

    Feature("Import process") {

        Scenario("When try to import process already imported should fail with ResourceAlreadyExistsException and expected message") {

            lateinit var processService: ProcessService
            lateinit var exception: Throwable
            lateinit var processRepository: ProcessRepository

            Given("A repository that says process already imported") {
                processRepository = mockk(relaxed = true)
                every { processRepository.processExists(any()) } returns true
            }

            And("A valid process service") {
                processService = ProcessServiceImpl(
                    mockk(relaxed = true), processRepository, mockk(relaxed = true), mockk(relaxed = true)
                )
            }

            When("Try to import process") {
                exception = assertFails {
                    processService.importProcess(ProcessImportRequest("0713663-77.2019.8.02.0001"))
                }
            }

            Then("Should fails with ResourceAlreadyExistsException") {
                assert(exception is ResourceAlreadyExistsException)
            }

            And("Expected message") {
                assertEquals("The resource already exists.", exception.message)
            }
        }

        Scenario("When try to import process that not exists in first degree should fail with ImportFailException and expected message") {

            lateinit var processService: ProcessService
            lateinit var exception: Throwable
            lateinit var crawlersOfFirstDegreeByCourt: Map<CourtOfJustice, CourtCrawlerFirstDegree>

            Given("A mocked map of crawlers") {
                crawlersOfFirstDegreeByCourt = mockk(relaxed = true)
            }

            And("A process does not exists in first degree") {
                every { crawlersOfFirstDegreeByCourt[any()]?.crawlFirstDegree(any()) } returns null
            }

            And("A valid process service") {
                processService = ProcessServiceImpl(
                    mockk(relaxed = true), mockk(relaxed = true), crawlersOfFirstDegreeByCourt, mockk(relaxed = true)
                )
            }

            When("Try to import process") {
                exception = assertFails {
                    processService.importProcess(ProcessImportRequest("0713663-77.2019.8.02.0001"))
                }
            }

            Then("Should fails with ImportFailException") {
                assert(exception is ImportFailException)
            }

            And("Expected message") {
                assertEquals("Some problem occurred when try to import court data.", exception.message)
            }
        }

        Scenario("When try to import process that exists only in first degree should should call the right components") {
            lateinit var processService: ProcessService
            lateinit var processRepository: ProcessRepository
            lateinit var crawlersOfFirstDegreeByCourt: Map<CourtOfJustice, CourtCrawlerFirstDegree>
            lateinit var crawlersOfSecondDegreeByCourt: Map<CourtOfJustice, CourtCrawlerSecondDegree>

            Given("A mocked dependencies") {
                crawlersOfFirstDegreeByCourt = mockk(relaxed = true)
                processRepository = mockk(relaxed = true)
                crawlersOfFirstDegreeByCourt = mockk(relaxed = true)
                crawlersOfSecondDegreeByCourt = mockk(relaxed = true)
            }

            And("A repository that says process not imported") {
                every { processRepository.processExists(any()) } returns false
            }

            And("A process exists in first degree") {
                every { crawlersOfFirstDegreeByCourt[any()]?.crawlFirstDegree(any()) } returns mockk()
            }

            And("A process does not exists in second degree") {
                every { crawlersOfSecondDegreeByCourt[any()]?.crawlSecondDegree(any()) } returns null
            }

            And("A valid process service") {
                processService = ProcessServiceImpl(
                    mockk(relaxed = true),
                    processRepository,
                    crawlersOfFirstDegreeByCourt,
                    crawlersOfSecondDegreeByCourt
                )
            }

            When("Try to import process") {
                processService.importProcess(ProcessImportRequest("0713663-77.2019.8.02.0001"))
            }

            Then("Should call the right components") {
                verifyOrder {
                    processRepository.processExists(any())
                    crawlersOfFirstDegreeByCourt[any()]?.crawlFirstDegree(any())
                    crawlersOfSecondDegreeByCourt[any()]?.crawlSecondDegree(any())
                    processRepository.save(any())
                }
            }
        }

        Scenario("When try to import process that exists in first and second degree should should call the right components") {
            lateinit var processService: ProcessService
            lateinit var processRepository: ProcessRepository
            lateinit var crawlersOfFirstDegreeByCourt: Map<CourtOfJustice, CourtCrawlerFirstDegree>
            lateinit var crawlersOfSecondDegreeByCourt: Map<CourtOfJustice, CourtCrawlerSecondDegree>

            Given("A mocked dependencies") {
                crawlersOfFirstDegreeByCourt = mockk(relaxed = true)
                processRepository = mockk(relaxed = true)
                crawlersOfFirstDegreeByCourt = mockk(relaxed = true)
                crawlersOfSecondDegreeByCourt = mockk(relaxed = true)
            }

            And("A repository that says process not imported") {
                every { processRepository.processExists(any()) } returns false
            }

            And("A process exists in first degree") {
                every { crawlersOfFirstDegreeByCourt[any()]?.crawlFirstDegree(any()) } returns mockk()
            }

            And("A process exists in second degree") {
                every { crawlersOfSecondDegreeByCourt[any()]?.crawlSecondDegree(any()) } returns mockk()
            }

            And("A valid process service") {
                processService = ProcessServiceImpl(
                    mockk(relaxed = true),
                    processRepository,
                    crawlersOfFirstDegreeByCourt,
                    crawlersOfSecondDegreeByCourt
                )
            }

            When("Try to import process") {
                processService.importProcess(ProcessImportRequest("0713663-77.2019.8.02.0001"))
            }

            Then("Should call the right components") {
                verifyOrder {
                    processRepository.processExists(any())
                    crawlersOfFirstDegreeByCourt[any()]?.crawlFirstDegree(any())
                    crawlersOfSecondDegreeByCourt[any()]?.crawlSecondDegree(any())
                    processRepository.save(any())
                }
            }
        }
    }

    Feature("Retrieve process") {

        Scenario("When try to retrieve process with invalid length should fail with InvalidProcessNumberException an expected message") {

            lateinit var processService: ProcessService
            lateinit var exception: Throwable

            Given("A valid process service") {
                processService = ProcessServiceImpl(
                    mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true)
                )
            }

            When("Try to retrieve process with invalid length") {
                exception = assertFails {
                    processService.retrieveProcess("123456")
                }
            }

            Then("Should fails with InvalidProcessNumberException") {
                assert(exception is InvalidProcessNumberException)
            }

            And("Expected message") {
                assertEquals("Process number size is invalid", exception.message)
            }
        }

        Scenario("When try to retrieve process with invalid length should call the right components") {

            lateinit var processService: ProcessService
            lateinit var processNumber: String
            lateinit var processRepository: ProcessRepository

            Given("A mocked ProcessImportProducer") {
                processRepository = mockk(relaxed = true)
            }

            And("A valid process number") {
                processNumber = "0713663-77.2019.8.02.0001"
            }

            And("A ProcessImportProducer that runs without error") {
                every { processRepository.retrieve(processNumber) } returns mockk()
            }

            Given("A valid process service") {
                processService = ProcessServiceImpl(
                    mockk(relaxed = true), processRepository, mockk(relaxed = true), mockk(relaxed = true)
                )
            }

            When("Try to retrieve process with valid length") {
                processService.retrieveProcess(processNumber)
            }

            Then("Should call the right components") {
                verifyOrder {
                    processRepository.retrieve(any())
                }
            }
        }
    }
})
