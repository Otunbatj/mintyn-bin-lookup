package com.testmintyn.binlookup

import com.testmintyn.binlookup.dto.UserDto
import com.testmintyn.binlookup.exception.NotFoundException
import com.testmintyn.binlookup.model.User
import com.testmintyn.binlookup.repository.UserRepository
import com.testmintyn.binlookup.service.impl.UserServiceImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

//@SpringBootTest
class UserServiceImplTest {

    private val userRepository: UserRepository = mockk();
    private val passwordEncoder: BCryptPasswordEncoder = mockk();
    private val userService = UserServiceImpl(userRepository, passwordEncoder);


    @Test
    fun createUserSuccessfully() {
        val userDto = UserDto("Test", "Test", "test@gmail.com", "08023234546", "Password@123")
        val expectedUser = User(2L, "Test2", "Test2", "test2@gmail.com", "08023234546","Password@123")

        every { userRepository.save(any()) } returns expectedUser
        every { passwordEncoder.encode(userDto.password) } returns "Password@123"

        val actualUser = userService.createUser(userDto);

        assertEquals(expectedUser.userId, actualUser.userId);
        assertEquals(expectedUser.password, actualUser.password);
        assertEquals(expectedUser.email, actualUser.email);
    }

    @Test
    fun createUserAlreadyExist() {
        val userDto = UserDto("Test", "Test", "test@gmail.com", "08023234546", "Password@123")
        val expectedUser = User(2L, "Test2", "Test2", "test2@gmail.com", "08023234546","Password@123")

        every { userRepository.save(any()) } throws NotFoundException("")
        every { passwordEncoder.encode(userDto.password) } returns "Password@123"

        val actualUser = userService.createUser(userDto);

        assertEquals(expectedUser.userId, actualUser.userId);
        assertEquals(expectedUser.password, actualUser.password);
        assertEquals(expectedUser.email, actualUser.email);
    }

    @Test
    fun getUserByEmail() {
    }

    @Test
    fun loadUserByUsername() {
    }
}