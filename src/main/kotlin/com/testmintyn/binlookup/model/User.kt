package com.testmintyn.binlookup.model

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long? = null,

    @field:NotBlank(message = "First name is required")
    var firstName: String? = null,

    @field:NotBlank(message = "Last name is required")
    var lastName: String? = null,

    @field:NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    var email: String? = null,

    @field:NotBlank(message = "PhoneNumber is required")
    @field:Size(min = 11, max = 11, message = "Phone number must be 11 characters")
    @Column(unique = true)
    var phoneNumber: String? = null,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*$",
        message = "Password must have at least one lowercase letter, one uppercase letter, one digit, and one special character"
    )
    var password: String? = null
)