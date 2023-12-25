package com.testmintyn.binlookup.service.impl

import com.testmintyn.binlookup.dto.UserDto
import com.testmintyn.binlookup.exception.GeneralException
import com.testmintyn.binlookup.exception.NotFoundException
import com.testmintyn.binlookup.exception.UserCreationException
import com.testmintyn.binlookup.model.User
import com.testmintyn.binlookup.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UserServiceImplTest {

    private val userRepository: UserRepository = mockk()
    private val passwordEncoder: BCryptPasswordEncoder = mockk()
    private val userService = UserServiceImpl(userRepository, passwordEncoder)


    @Test
    fun createUserSuccessfully() {
        val userDto = UserDto("Test", "Test", "test@gmail.com", "08023234546", "Password@123")
        val expectedUser = User(2L, "Test2", "Test2", "test2@gmail.com", "08023234546", "Password@123")

        every { userRepository.save(any()) } returns expectedUser
        every { passwordEncoder.encode(userDto.password) } returns "Password@123"

        val actualUser = userService.createUser(userDto)

        assertEquals(expectedUser.userId, actualUser.userId)
        assertEquals(expectedUser.password, actualUser.password)
        assertEquals(expectedUser.email, actualUser.email)
    }

    @Test
    fun createUserAlreadyExistThrowsExcption() {
        val userDto = UserDto("Test", "Test", "test@gmail.com", "08023234546", "Password@123")

        every { userRepository.save(any()) } throws DataIntegrityViolationException("")
        every { passwordEncoder.encode(userDto.password) } returns "Password@123"

        assertThrows<UserCreationException> {
            userService.createUser(userDto)
        }
    }

    @Test
    fun getUserByEmailSuccessfully() {
        val userDto = UserDto("Test", "Test", "test@gmail.com", "08023234546", "Password@123")
        val expectedUser = User(2L, "Test2", "Test2", "test2@gmail.com", "08023234546", "Password@123")

        every { userRepository.findByEmail(any()) } returns expectedUser

        val actualUser = userService.getUserByEmail(userDto.email)

        assertEquals(expectedUser.userId, actualUser.userId)
        assertEquals(expectedUser.email, actualUser.email)
        assertEquals(expectedUser.lastName, actualUser.lastName)
        assertEquals(expectedUser.firstName, actualUser.firstName)
    }

    @Test
    fun getUserByEmailDoesNotExist() {
        val userDto = UserDto("Test", "Test", "test@gmail.com", "08023234546", "Password@123")

        every { userRepository.findByEmail(any()) } returns null

        assertThrows<NotFoundException> {
            userService.getUserByEmail(userDto.email)
        }
    }

    @Test
    fun loadUserByUsernameInvalidEmail() {
        assertThrows<GeneralException> {
            userService.loadUserByUsername(null)
        }
    }
}