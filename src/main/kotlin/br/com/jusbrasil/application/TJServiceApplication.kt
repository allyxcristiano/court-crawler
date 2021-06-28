package br.com.jusbrasil.application

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import br.com.jusbrasil.application.configurations.ObjectMapperBuilder
import br.com.jusbrasil.application.configurations.dimodules.ConfigurationModule
import br.com.jusbrasil.application.configurations.dimodules.ControllersModule
import br.com.jusbrasil.application.configurations.dimodules.CrawlerModule
import br.com.jusbrasil.application.configurations.dimodules.DatasourceModule
import br.com.jusbrasil.application.configurations.dimodules.MessagingModule
import br.com.jusbrasil.application.configurations.dimodules.RepositoriesModule
import br.com.jusbrasil.application.configurations.dimodules.RoutesModule
import br.com.jusbrasil.application.configurations.dimodules.ServicesModule
import br.com.jusbrasil.application.processimport.web.routes.ProcessRoutes
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.openqa.selenium.remote.RemoteWebDriver

class TJServiceApplication : KoinComponent {
    private val environmentConfiguration: EnvironmentConfiguration by inject()
    private val processRoutes: ProcessRoutes by inject()
    private val driver: RemoteWebDriver by inject()

    fun startApplication(): Javalin {
        setupDependencyInjection()

        JavalinJackson.configure(ObjectMapperBuilder.getMapper())

        return Javalin.create { config ->
            config.registerPlugin(OpenApiPlugin(openApiOptions()))
        }.routes {
            processRoutes.register()
        }.events { eventListener ->
            eventListener.serverStopped {
                driver.quit()
                stopKoin()
            }
        }.start(environmentConfiguration.serverPort)
    }

    private fun setupDependencyInjection() {
        StandAloneContext.startKoin(
            list = listOf(
                RoutesModule.modules(),
                ControllersModule.modules(),
                ConfigurationModule.modules(),
                ServicesModule.modules(),
                RepositoriesModule.modules(),
                MessagingModule.modules(),
                DatasourceModule.modules(),
                CrawlerModule.modules()
            ),
            useEnvironmentProperties = true
        )
    }

    private fun openApiOptions(): OpenApiOptions {
        val applicationInfo = Info().version("1.0")
            .title("Court Crawler")
            .description("Trying to dominate the world crawling the courts")
        return OpenApiOptions(applicationInfo).path("/docs-json")
            .swagger(SwaggerOptions("/docs").title("Court Crawler"))
    }
}
