package br.com.jusbrasil.domain.processimport.vos

import br.com.jusbrasil.domain.processimport.vos.enums.CourtOfJustice
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class Process(
    val number: String,
    val courtOfJustice: CourtOfJustice,
    val firstDegree: FirstDegree,
    val secondDegree: SecondDegree? = null
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class FirstDegree(
    val grade: String,
    val area: String,
    val subject: String? = null,
    val distributionDate: LocalDate? = null,
    val judge: String,
    val value: String,
    val partsOfProcess: List<String>,
    val historyOfDegree: List<HistoryOfDegree>? = null
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class SecondDegree(
    val grade: String,
    val area: String,
    val subject: String? = null,
    val partsOfProcess: List<String>,
    val historyOfDegree: List<HistoryOfDegree>? = null
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class HistoryOfDegree(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    val date: LocalDate,
    val description: String
)
