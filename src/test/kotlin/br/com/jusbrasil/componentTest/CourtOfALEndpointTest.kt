package br.com.jusbrasil.componentTest

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import br.com.jusbrasil.componentTest.commons.TestEnvironmentHelper
import com.github.kittinunf.fuel.Fuel
import com.natpryce.konfig.Configuration
import com.natpryce.konfig.EnvironmentVariables
import org.awaitility.Duration
import org.awaitility.kotlin.await
import org.awaitility.kotlin.withPollInterval
import org.eclipse.jetty.http.HttpStatus
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

object CourtOfALEndpointTest : Spek({

    val environmentHelper = TestEnvironmentHelper()
    val configuration: Configuration = EnvironmentVariables()
    val host = "http://localhost:${configuration[EnvironmentConfiguration.SERVER_PORT]}"

    beforeGroup {
        environmentHelper.provideAMongoInstance()
            .mockDatasourceDependencyInjectionModule()
            .setupAQueueInstance()
            .startApplication()
    }

    afterGroup {
        environmentHelper.tearDown(true)
    }

    Feature("Importing process of court of AL") {

        Scenario("When process can be accessed it should be imported successfully") {

            lateinit var processNumber: String

            Given("A clean test state") {
                environmentHelper.prepareTest()
            }

            And("A valid process") {
                processNumber = "07047069720138020001"
            }

            When("Send request to import a process") {
                Fuel.post("$host/processes/import").body("""{ "process_number" : "$processNumber" }""").response()
            }

            Then("Process can be retrieve successfully") {
                await.atMost(30, TimeUnit.SECONDS)
                    .withPollInterval(Duration.TWO_SECONDS)
                    .untilAsserted {
                        val response = Fuel.get("$host/processes/$processNumber").response()
                        assertEquals(HttpStatus.OK_200, response.second.statusCode)
                        assertNotNull(response.second.data)
                    }
            }
        }
    }

    Feature("Importing process of court of MS") {

        Scenario("When process can be accessed it should be imported successfully") {

            lateinit var processNumber: String

            Given("A clean test state") {
                environmentHelper.prepareTest()
            }

            And("A valid process") {
                processNumber = "00277149819958120001"
            }

            When("Send request to import a process") {
                Fuel.post("$host/processes/import").body("""{ "process_number" : "$processNumber" }""").response()
            }

            Then("Process can be retrieve successfully") {
                await.atMost(30, TimeUnit.SECONDS)
                    .withPollInterval(Duration.TWO_SECONDS)
                    .untilAsserted {
                        val response = Fuel.get("$host/processes/$processNumber").response()
                        assertEquals(HttpStatus.OK_200, response.second.statusCode)
                        assertNotNull(response.second.data)
                    }
            }
        }
    }
})
