package br.com.jusbrasil.domain.exceptions

import br.com.jusbrasil.domain.exceptions.enums.DomainErrors

abstract class DomainError(
    val type: DomainErrors = DomainErrors.INTERNAL_ERROR,
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)
