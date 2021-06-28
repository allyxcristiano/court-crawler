package br.com.jusbrasil.infrastructure

object Regex {

    fun dateRegexWithDayMonthAndYear() = """^\d{1,2}\/\d{1,2}\/\d{4}${'$'}""".toRegex()
}
