package br.com.jusbrasil.domain.exceptions

import br.com.jusbrasil.domain.exceptions.enums.DomainErrors

class ElementNotFoundException(
    element: String,
    override val message: String = "Element $element not found."
) : DomainError(DomainErrors.EXTERNAL_ERROR, message)
