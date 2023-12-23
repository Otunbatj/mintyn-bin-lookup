package com.testmintyn.binlookup

import com.google.gson.Gson
import com.testmintyn.binlookup.dto.Response
import com.testmintyn.binlookup.dto.UserDto
import com.testmintyn.binlookup.exception.NotFoundException
import com.testmintyn.binlookup.model.User
import com.testmintyn.binlookup.repository.UserRepository
import com.testmintyn.binlookup.service.AppService
import com.testmintyn.binlookup.service.UserService
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.net.URI
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension::class)
class AppServiceTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var appService: AppService


    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var gson: Gson

    @BeforeAll
    fun setup() {
        // This method runs once before all tests
        // Create and save a default user to the repository
        val userDto = UserDto("Test", "Test", "test@gmail.com", "08023234546", "Password@123")
        val response = restTemplate.exchange(
            "/api/v1/users/register",
            HttpMethod.POST,
            HttpEntity(userDto),
            Response::class.java
        )

        println(response.body)
    }


    @Test
    fun testFindByEmail() {
        val defaultUser = userRepository.findByEmail("test@gmail.com")
        assertEquals("test@gmail.com", defaultUser?.email)
    }

    @Test
    fun testFindUserService() {
        val defaultUser = userService.getUserByEmail("test@gmail.com")
        assertNotNull(defaultUser)
    }

    @Test
    fun testCreateUserService() {
        val defaultUser =
            User(2L, "Test2", "Test2", "test2@gmail.com", "08023234546", passwordEncoder.encode("Password@123"))
        val newUser = userService.createUser(defaultUser)
        assertEquals(defaultUser, newUser)
    }

    @Test
    fun testCreateUserAppService() {
        val userDto =
            UserDto("Test 3", "Test 3", "test3@gmail.com", "08023098912", passwordEncoder.encode("Password@123"))
        val response = appService.createUser(userDto)
        assertNotNull(response)
        assertEquals("00", response.responseCode)
    }

    @Test
    fun testInvalidUserEmail() {
        val wrongEmail = "wrongemail@gmail.com"
        assertFailsWith<NotFoundException> {
            appService.getUser(wrongEmail)
        }
    }

    @Test
    fun testGetInvalidUserFromGetUserEndPoint() {
        val wrongEmail = "wrongemail@gmail.com"
        val responseEntity = restTemplate
            .withBasicAuth("test@gmail.com", "Password@123")
            .exchange(
                RequestEntity<Any>(HttpMethod.GET, URI.create("/api/v1/users/single/$wrongEmail")),
                String::class.java
            )

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.statusCode)
    }

    @Test
    fun testGetValidUserFromGetUserEndPoint() {
        val validEmail = "test@gmail.com"
        val responseEntity = restTemplate
            .withBasicAuth("test@gmail.com", "Password@123")
            .exchange(
                RequestEntity<Any>(HttpMethod.GET, URI.create("/api/v1/users/single/$validEmail")),
                String::class.java
            )

        assertEquals(HttpStatus.OK, responseEntity.statusCode)
    }

    @Test
    fun testGetInvalidEmailFromGetUserEndPoint() {
        val wrongEmail = "wrongemailgmail.com"
        val responseEntity = restTemplate
            .withBasicAuth("test@gmail.com", "Password@123")
            .exchange(
                RequestEntity<Any>(HttpMethod.GET, URI.create("/api/v1/users/single/$wrongEmail")),
                String::class.java
            )
        val response = gson.fromJson(responseEntity.body, Response::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.statusCode)
        assertContains(response.obj.toString(), "Invalid email format")
    }

    @Test
    fun testIncorrectBINLengthFromLookupBinEndPoint() {
        val bin = "12345"
        val responseEntity = restTemplate
            .withBasicAuth("test@gmail.com", "Password@123")
            .exchange(
                RequestEntity<Any>(HttpMethod.GET, URI.create("/api/v1/card-scheme/verify/$bin")),
                String::class.java
            )

        val response = gson.fromJson(responseEntity.body, Response::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.statusCode)
        assertContains(response.obj.toString(), "BIN must be either 6 or 8 characters")
    }


}