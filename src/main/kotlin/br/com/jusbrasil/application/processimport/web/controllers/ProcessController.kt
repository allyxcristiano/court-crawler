package br.com.jusbrasil.application.processimport.web.controllers

import br.com.jusbrasil.application.processimport.dtos.request.ProcessImportRequest
import br.com.jusbrasil.domain.processimport.services.ProcessService
import br.com.jusbrasil.domain.processimport.vos.Process
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.http.HttpStatus

class ProcessController(
    private val processService: ProcessService
) {
    @OpenApi(
        description = "This endpoint provides a channel to start import of a process from court of AL or MS.",
        requestBody = OpenApiRequestBody(content = arrayOf(OpenApiContent(from = ProcessImportRequest::class))),
        responses = [
            OpenApiResponse(status = "202"),
            OpenApiResponse(status = "400"),
            OpenApiResponse(status = "500")
        ]
    )
    fun import(ctx: Context) {
        val processImportRequest = ctx.body<ProcessImportRequest>()
        processService.produceMessageToImportProcess(processImportRequest)
        ctx.status(HttpStatus.ACCEPTED_202)
        ctx.header(HttpHeader.LOCATION.name, "/processes/${processImportRequest.processNumber}")
    }

    @OpenApi(
        description = "This endpoint provides a channel retrieve a successfully imported process from court of AL or MS.",
        responses = [
            OpenApiResponse(status = "200", content = arrayOf(OpenApiContent(Process::class))),
            OpenApiResponse(status = "400"),
            OpenApiResponse(status = "500")
        ]
    )
    fun retrieve(ctx: Context) {
        val processNumber = ctx.pathParam(PROCESS_NUMBER_PARAM)
        val process = processService.retrieveProcess(processNumber)

        if (process == null) {
            ctx.status(HttpStatus.NOT_FOUND_404)
        } else {
            ctx.json(process)
        }
    }

    @OpenApi(
        description = "This endpoint provides a channel retrieve all successfully imported processes from court of " +
            "AL or MS. This endpoint was only made available for easy data visualization, if we were talking about " +
            "a production context this endpoint would be paged.",
        responses = [
            OpenApiResponse(status = "200", content = arrayOf(OpenApiContent(Process::class, true))),
            OpenApiResponse(status = "400"),
            OpenApiResponse(status = "500")
        ]
    )
    fun retrieveAll(ctx: Context) {
        ctx.json(processService.retrieve())
    }

    companion object {
        private const val PROCESS_NUMBER_PARAM = "process-number"
    }
}
