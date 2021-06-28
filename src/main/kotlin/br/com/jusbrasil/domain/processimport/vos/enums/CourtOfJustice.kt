package br.com.jusbrasil.domain.processimport.vos.enums

import br.com.jusbrasil.domain.exceptions.InvalidDataException

enum class CourtOfJustice(val number: String) {
    COURT_OF_JUSTICE_OF_AL("02"),
    COURT_OF_JUSTICE_OF_MS("12");

    companion object {
        fun valueOrDomainException(value: String): CourtOfJustice {
            return when (value) {
                COURT_OF_JUSTICE_OF_AL.number -> COURT_OF_JUSTICE_OF_AL
                COURT_OF_JUSTICE_OF_MS.number -> COURT_OF_JUSTICE_OF_MS
                else -> throw InvalidDataException(
                    """
                        The given court of justice is invalid or unsupported. Please give one of the valid 
                        set: ${values().map { it.name }}
                    """.trimIndent()
                )
            }
        }
    }
}
