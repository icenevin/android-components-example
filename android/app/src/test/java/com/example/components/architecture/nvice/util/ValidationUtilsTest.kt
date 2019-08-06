package com.example.components.architecture.nvice.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun validateEmail_ValidEmail_ReturnTrue(){
        assertThat(ValidationUtils.isValidEmail("aaa@aaa.com")).isTrue()
    }

    @Test
    fun validateEmail_InvalidEmail_ReturnFalse() {
        assertThat(ValidationUtils.isValidEmail("aaa")).isFalse()
    }

    @Test
    fun validateCitizenId_ValidCitizenId_ReturnTrue(){
        assertThat(ValidationUtils.isValidCitizenId("1234567890121")).isTrue()
    }

    @Test
    fun validateCitizenId_InvalidCitizenId_ReturnFalse(){
        assertThat(ValidationUtils.isValidCitizenId("1234567890122")).isFalse()
    }

    @Test
    fun validateCitizenId_TooLongCitizenId_ReturnFalse(){
        assertThat(ValidationUtils.isValidCitizenId("12345678901211")).isFalse()
    }

    @Test
    fun validateCitizenId_TooShortCitizenId_ReturnFalse(){
        assertThat(ValidationUtils.isValidCitizenId("123456789")).isFalse()
    }

    @Test
    fun validateCitizenId_HaveCharactersInCitizenId_ReturnFalse(){
        assertThat(ValidationUtils.isValidCitizenId("a234567b90121")).isFalse()
    }
}