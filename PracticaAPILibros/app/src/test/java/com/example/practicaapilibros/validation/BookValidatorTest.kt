package com.example.practicaapilibros.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BookValidatorTest {

    @Test
    fun acceptsHttpAndHttpsUrls() {
        assertTrue(BookValidator.isValidUrl("https://encantalibros.com/wp-content/uploads/2020/12/9788498386363.jpg"))
        assertTrue(BookValidator.isValidUrl("http://example.com/cover.png"))
    }

    @Test
    fun rejectsInvalidUrls() {
        assertFalse(BookValidator.isValidUrl("encantalibros.com/image.jpg"))
        assertFalse(BookValidator.isValidUrl("ftp://example.com/cover.png"))
        assertFalse(BookValidator.isValidUrl("https://"))
        assertFalse(BookValidator.isValidUrl("https://exa mple.com"))
    }

    @Test
    fun acceptsValidIsbn13WithOrWithoutHyphens() {
        assertTrue(BookValidator.isValidIsbn("9788498386363"))
        assertTrue(BookValidator.isValidIsbn("978-84-9838-636-3"))
    }

    @Test
    fun acceptsValidIsbn10IncludingXCheckDigit() {
        assertTrue(BookValidator.isValidIsbn("0132350882"))
        assertTrue(BookValidator.isValidIsbn("080442957X"))
    }

    @Test
    fun rejectsIsbnWithWrongChecksumOrLength() {
        assertFalse(BookValidator.isValidIsbn("9788498386364"))
        assertFalse(BookValidator.isValidIsbn("0132350883"))
        assertFalse(BookValidator.isValidIsbn("12345"))
        assertFalse(BookValidator.isValidIsbn("97884983863AB"))
    }

    @Test
    fun normalizeIsbnRemovesHyphensAndSpaces() {
        assertEquals("9788498386363", BookValidator.normalizeIsbn(" 978-84 9838-636-3 "))
        assertEquals("080442957X", BookValidator.normalizeIsbn("0-8044-2957-x"))
    }

    @Test
    fun validateIsbnReportsLengthAfterNormalizing() {
        assertEquals(IsbnValidation.InvalidLength(5), BookValidator.validateIsbn("12-345"))
        assertEquals(IsbnValidation.InvalidLength(12), BookValidator.validateIsbn("978849838636"))
    }

    @Test
    fun validateIsbnReportsInvalidCharactersPerType() {
        assertEquals(
            IsbnValidation.InvalidCharacters(IsbnType.ISBN_10),
            BookValidator.validateIsbn("01323X0882")
        )
        assertEquals(
            IsbnValidation.InvalidCharacters(IsbnType.ISBN_13),
            BookValidator.validateIsbn("978849838636X")
        )
    }

    @Test
    fun validateIsbnReportsInvalidChecksumPerType() {
        assertEquals(
            IsbnValidation.InvalidChecksum(IsbnType.ISBN_10),
            BookValidator.validateIsbn("0132350883")
        )
        assertEquals(
            IsbnValidation.InvalidChecksum(IsbnType.ISBN_13),
            BookValidator.validateIsbn("9788498386364")
        )
    }

    @Test
    fun validateIsbnReturnsValidForCorrectIsbn() {
        assertEquals(IsbnValidation.Valid, BookValidator.validateIsbn("978-84-9838-636-3"))
    }
}
