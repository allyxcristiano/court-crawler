package br.com.jusbrasil.domain.processimport.repositories

import br.com.jusbrasil.domain.processimport.vos.Process

interface ProcessRepository {

    fun processExists(processNumber: String): Boolean

    fun save(process: Process)

    fun retrieve(processNumber: String): Process?

    fun retrieve(): List<Process>
}
