package com.testmintyn.binlookup.controller

import com.testmintyn.binlookup.dto.UserDto
import com.testmintyn.binlookup.service.AppService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Email

@Validated
@RestController
@RequestMapping("/api/v1/")
class UserController {
    @Autowired
    lateinit var appService: AppService

    @PostMapping("/users/register")
    fun addUsers(@Valid @RequestBody userDto: UserDto): ResponseEntity<Any> {
        return ResponseEntity.ok(appService.createUser(userDto))
    }

    @GetMapping("/users/single/{email}")
    fun getUser(@PathVariable("email") @Email(message = "Invalid email format") email: String): ResponseEntity<Any> {
        return ResponseEntity.ok(appService.getUser(email))
    }
}