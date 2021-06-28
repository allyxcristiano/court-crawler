package br.com.jusbrasil.application.configurations

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object ObjectMapperBuilder {
    fun getMapper(): ObjectMapper = defaultObjectMapper

    private val defaultObjectMapper = jacksonObjectMapper().apply {
        configure()
    }

    private fun ObjectMapper.configure(namingStrategy: PropertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE) {
        propertyNamingStrategy = namingStrategy
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        registerModule(JavaTimeModule())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        dateFormat = StdDateFormat()
    }
}
