package br.com.jusbrasil.domain.processimport.repositories.implementations

import br.com.jusbrasil.domain.exceptions.ResourceAlreadyExistsException
import br.com.jusbrasil.domain.processimport.repositories.ProcessRepository
import br.com.jusbrasil.domain.processimport.vos.Process
import br.com.jusbrasil.infrastructure.datasources.DataSource
import com.mongodb.MongoWriteException
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import org.koin.core.Koin.Companion.logger
import org.litote.kmongo.findOne
import org.litote.kmongo.toList

class ProcessRepositoryImpl(private val dataSource: DataSource<MongoDatabase>) : ProcessRepository {

    private val collection by lazy {
        dataSource.getDatabase().getCollection(COLLECTION, Process::class.java).apply {
            createIndex(
                Indexes.compoundIndex(
                    Indexes.ascending("number")
                ),
                IndexOptions().unique(true)
            )
        }
    }

    override fun processExists(processNumber: String): Boolean {
        return collection.countDocuments(eq("number", processNumber)) > 0L
    }

    override fun save(process: Process) {
        try {
            collection.insertOne(process)
        } catch (ex: MongoWriteException) {
            if (MONGODB_ERROR_DUPLICATED_KEY == ex.error.code) {
                val message = "Was not possible to create a new Process with number ${process.number} " +
                    "in database due to constraint violation."
                logger.err(message)
                throw ResourceAlreadyExistsException(message)
            }

            throw ex
        }
    }

    override fun retrieve(processNumber: String): Process? {
        return collection.findOne(eq("number", processNumber))
    }

    override fun retrieve(): List<Process> {
        return collection.find().toList()
    }

    companion object {
        private const val COLLECTION = "PROCESS"
        private const val MONGODB_ERROR_DUPLICATED_KEY = 11000
    }
}
