package br.com.jusbrasil.domain.exceptions

import br.com.jusbrasil.domain.exceptions.enums.DomainErrors

class RelatedProcessException(
    override val message: String = "This process is cited by others but no contains any additional information."
) : DomainError(DomainErrors.EXTERNAL_ERROR, message)
