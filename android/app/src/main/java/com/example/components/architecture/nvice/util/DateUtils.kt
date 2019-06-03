package com.example.components.architecture.nvice.util

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter

class DateUtils {

    companion object {

        private const val DEFAULT_FORMAT = "d MMM yyyy"

        fun now(): LocalDate = LocalDate.now()

        fun nowInString(): String = format(now())

        fun nowInString(format: String): String {
            return format(now(), format)
        }

        fun nowInMillis(): Long = Instant.now().toEpochMilli()

        fun nowInMillis(additionalYear: Long): Long {
            val now = now()
            return if (additionalYear >= 0) {
                now.plusYears(additionalYear).getMillis()
            } else {
                now.minusYears(additionalYear * -1).getMillis()
            }
        }

        fun format(date: LocalDate): String = format(date, DEFAULT_FORMAT)

        fun format(date: LocalDate, format: String): String = date.format(DateTimeFormatter.ofPattern(format))

        fun parse(text: CharSequence?): LocalDate = parse(text, DEFAULT_FORMAT)

        fun parse(text: CharSequence?, format: String): LocalDate = LocalDate.parse(text, DateTimeFormatter.ofPattern(format))

        fun parseToMillis(text: CharSequence?): Long = parse(text).getMillis()

        fun parseToMillis(text: CharSequence?, format: String): Long = parse(text, format).getMillis()
    }
}

fun LocalDate.getMillis() = this.atTime(LocalTime.now(ZoneId.systemDefault())).toInstant(OffsetDateTime.now().offset).toEpochMilli()