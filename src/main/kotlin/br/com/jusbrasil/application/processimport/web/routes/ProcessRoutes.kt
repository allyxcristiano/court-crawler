package br.com.jusbrasil.application.processimport.web.routes

import br.com.jusbrasil.application.processimport.web.controllers.ProcessController
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post

class ProcessRoutes(private val processController: ProcessController) {
    fun register() {
        path("processes") {
            path("import") {
                post(processController::import)
            }

            path(":process-number") {
                get(processController::retrieve)
            }

            get(processController::retrieveAll)
        }
    }
}
