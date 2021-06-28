package br.com.jusbrasil.domain.processimport.services

import br.com.jusbrasil.application.processimport.dtos.request.ProcessImportRequest
import br.com.jusbrasil.domain.processimport.vos.Process

interface ProcessService {
    fun produceMessageToImportProcess(request: ProcessImportRequest)

    fun importProcess(request: ProcessImportRequest)

    fun retrieveProcess(processNumber: String): Process?

    fun retrieve(): List<Process>
}
