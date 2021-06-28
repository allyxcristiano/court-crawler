package br.com.jusbrasil.domain.exceptions

import br.com.jusbrasil.domain.exceptions.enums.DomainErrors

class ImportFailException(
    override val message: String = "Some problem occurred when try to import court data."
) : DomainError(DomainErrors.EXTERNAL_ERROR, message)
