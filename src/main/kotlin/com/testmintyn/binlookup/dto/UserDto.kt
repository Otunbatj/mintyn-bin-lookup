package com.testmintyn.binlookup.dto

import org.jetbrains.annotations.NotNull
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size


data class UserDto(
    @field:NotBlank(message = "First name is required")
    @field:NotNull(value = "First name is required")
    var firstName: String,
    @field:NotBlank(message = "Last name is required")
    @field:NotNull(value = "Last name is required")
    var lastName: String,
    @field:NotBlank(message = "Email is required")
    @field:NotNull(value = "Email is required")
    @field:Email(message = "Invalid email format")
    var email: String,
    @field:NotBlank(message = "PhoneNumber is required")
    @field:NotNull(value = "PhoneNumber is required")
    @field:Size(min = 11, message = "Phone number must be 11 characters")
    var phoneNumber: String,
    @field:NotBlank(message = "Password is required")
    @field:NotNull(value = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*$",
        message = "Password must have at least one lowercase letter, one uppercase letter, one digit, and one special character"
    )
    var password: String
)
