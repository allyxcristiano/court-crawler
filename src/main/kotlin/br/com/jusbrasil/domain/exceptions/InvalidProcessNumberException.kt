package br.com.jusbrasil.domain.exceptions

import br.com.jusbrasil.domain.exceptions.enums.DomainErrors

class InvalidProcessNumberException(
    override val message: String = "Process number size is invalid"
) : DomainError(DomainErrors.INVALID_DATA, message)
