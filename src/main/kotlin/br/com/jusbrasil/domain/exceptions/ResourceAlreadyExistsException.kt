package br.com.jusbrasil.domain.exceptions

import br.com.jusbrasil.domain.exceptions.enums.DomainErrors

class ResourceAlreadyExistsException(
    override val message: String = "The resource already exists."
) : DomainError(DomainErrors.RESOURCE_ALREADY_EXISTS, message)
