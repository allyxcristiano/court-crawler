package br.com.jusbrasil.unit.application.processimport.web.controllers

import br.com.jusbrasil.application.processimport.dtos.request.ProcessImportRequest
import br.com.jusbrasil.application.processimport.web.controllers.ProcessController
import br.com.jusbrasil.domain.processimport.services.ProcessService
import br.com.jusbrasil.domain.processimport.vos.Process
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.http.HttpStatus
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object ProcessControllerTest : Spek({

    Feature("Import process") {

        Scenario("When try to import process should call the right components") {

            lateinit var processService: ProcessService
            lateinit var processController: ProcessController
            lateinit var context: Context
            lateinit var request: ProcessImportRequest

            Given("A valid context") {
                context = mockk(relaxed = true)
            }

            And("A valid request returned from context") {
                request = ProcessImportRequest("0713663-77.2019.8.02.0001")
                every { context.body<ProcessImportRequest>() } returns request
            }

            And("A mocked process service") {
                processService = mockk(relaxed = true)
            }

            And("A process controller") {
                processController = ProcessController(processService)
            }

            When("Try to import a process") {
                processController.import(context)
            }

            Then("Should call the right components") {
                verifyOrder {
                    context.body<ProcessImportRequest>()
                    processService.produceMessageToImportProcess(request)
                    context.status(HttpStatus.ACCEPTED_202)
                    context.header(HttpHeader.LOCATION.name, "/processes/${request.processNumber}")
                }
            }
        }
    }

    Feature("Retrieve process") {

        Scenario("When try to retrieve process should call the right components") {

            lateinit var processService: ProcessService
            lateinit var processController: ProcessController
            lateinit var context: Context
            lateinit var processNumber: String
            lateinit var process: Process

            Given("A valid context") {
                context = mockk(relaxed = true)
            }

            And("A valid process number returned from context") {
                processNumber = "0713663-77.2019.8.02.0001"
                every { context.pathParam("process-number") } returns processNumber
            }

            And("A mocked process service") {
                processService = mockk(relaxed = true)
            }

            And("A process controller") {
                processController = ProcessController(processService)
            }

            And("A mocked process return") {
                process = mockk(relaxed = true)
                every { processService.retrieveProcess(processNumber) } returns process
            }

            When("Try to import a process") {
                processController.retrieve(context)
            }

            Then("Should call the right components") {
                verifyOrder {
                    context.pathParam("process-number")
                    processService.retrieveProcess(processNumber)
                    context.json(process)
                }
            }
        }
    }
})
