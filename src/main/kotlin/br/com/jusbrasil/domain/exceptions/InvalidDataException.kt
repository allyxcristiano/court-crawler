package br.com.jusbrasil.domain.exceptions

import br.com.jusbrasil.domain.exceptions.enums.DomainErrors

private const val MESSAGE = "Some data is not in the set of allowed values."

class InvalidDataException(
    override val message: String = MESSAGE
) : DomainError(DomainErrors.INVALID_DATA, message)
