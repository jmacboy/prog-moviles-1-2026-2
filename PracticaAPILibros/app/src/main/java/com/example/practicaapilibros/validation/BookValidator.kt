package com.example.practicaapilibros.validation

import java.net.URI

object BookValidator {

    fun isValidUrl(value: String): Boolean {
        val uri = try {
            URI(value.trim())
        } catch (e: Exception) {
            return false
        }
        val scheme = uri.scheme?.lowercase()
        return (scheme == "http" || scheme == "https") && !uri.host.isNullOrBlank()
    }

    fun normalizeIsbn(value: String): String =
        value.filterNot { it == '-' || it.isWhitespace() }.uppercase()

    fun isValidIsbn(value: String): Boolean = validateIsbn(value) == IsbnValidation.Valid

    fun validateIsbn(value: String): IsbnValidation {
        val isbn = normalizeIsbn(value)
        return when (isbn.length) {
            10 -> validateIsbn10(isbn)
            13 -> validateIsbn13(isbn)
            else -> IsbnValidation.InvalidLength(isbn.length)
        }
    }

    private fun validateIsbn10(isbn: String): IsbnValidation {
        val last = isbn.last()
        if (!isbn.take(9).all { it.isDigit() } || (!last.isDigit() && last != 'X')) {
            return IsbnValidation.InvalidCharacters(IsbnType.ISBN_10)
        }
        val sum = isbn.mapIndexed { index, char ->
            val digit = if (char == 'X') 10 else char.digitToInt()
            digit * (10 - index)
        }.sum()
        return if (sum % 11 == 0) IsbnValidation.Valid
        else IsbnValidation.InvalidChecksum(IsbnType.ISBN_10)
    }

    private fun validateIsbn13(isbn: String): IsbnValidation {
        if (!isbn.all { it.isDigit() }) {
            return IsbnValidation.InvalidCharacters(IsbnType.ISBN_13)
        }
        val sum = isbn.mapIndexed { index, char ->
            char.digitToInt() * if (index % 2 == 0) 1 else 3
        }.sum()
        return if (sum % 10 == 0) IsbnValidation.Valid
        else IsbnValidation.InvalidChecksum(IsbnType.ISBN_13)
    }
}

enum class IsbnType { ISBN_10, ISBN_13 }

sealed interface IsbnValidation {
    data object Valid : IsbnValidation
    data class InvalidLength(val length: Int) : IsbnValidation
    data class InvalidCharacters(val type: IsbnType) : IsbnValidation
    data class InvalidChecksum(val type: IsbnType) : IsbnValidation
}
