package br.com.jusbrasil.domain.exceptions

import br.com.jusbrasil.domain.exceptions.enums.DomainErrors

class ProcessProtectedException : DomainError(DomainErrors.EXTERNAL_ERROR, "Unable to access data.")
