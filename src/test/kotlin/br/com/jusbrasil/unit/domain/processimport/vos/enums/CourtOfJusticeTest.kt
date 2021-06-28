package br.com.jusbrasil.unit.domain.processimport.vos.enums

import br.com.jusbrasil.domain.exceptions.InvalidDataException
import br.com.jusbrasil.domain.processimport.vos.enums.CourtOfJustice
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

object CourtOfJusticeTest : Spek({

    Feature("get value of knowledge court of justice") {

        Scenario("when try to get value of knowledge court of justice should retrieve as expected") {
            lateinit var courtOfJustices: List<CourtOfJustice>
            lateinit var courtOfJusticesReturned: MutableList<CourtOfJustice>

            Given("A list of courts of justice") {
                courtOfJustices = CourtOfJustice.values().toList()
            }

            And("A list to store results") {
                courtOfJusticesReturned = mutableListOf()
            }

            When("Try to get value of all types") {
                courtOfJustices.forEach {
                    courtOfJusticesReturned.add(CourtOfJustice.valueOrDomainException(it.number))
                }
            }

            Then("Should return courts of justice as expected") {
                assertEquals(courtOfJustices.size, courtOfJusticesReturned.size)

                courtOfJusticesReturned.forEach {
                    assertTrue(courtOfJustices.contains(it))
                }
            }
        }

        Scenario("when try to get value of unknowable court of justice should fails with InvalidDataException and expected message") {
            lateinit var invalidCourtOfJustice: String
            lateinit var exception: Throwable

            Given("An invalid court of justice") {
                invalidCourtOfJustice = "SomeInvalid"
            }

            When("Try to get value of all types") {
                exception = assertFails {
                    CourtOfJustice.valueOrDomainException(invalidCourtOfJustice)
                }
            }

            Then("Should fails with InvalidDataException") {
                assert(exception is InvalidDataException)
            }

            And("Expected message") {
                assertEquals(
                    """
                        The given court of justice is invalid or unsupported. Please give one of the valid 
                        set: ${CourtOfJustice.values().map { it.name }}
                    """.trimIndent(),
                    exception.message
                )
            }
        }
    }
})
