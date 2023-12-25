package com.testmintyn.binlookup.service.impl

import com.testmintyn.binlookup.dto.UserDto
import com.testmintyn.binlookup.exception.GeneralException
import com.testmintyn.binlookup.exception.NotFoundException
import com.testmintyn.binlookup.exception.UserCreationException
import com.testmintyn.binlookup.extensions.Extensions.toUserDetails
import com.testmintyn.binlookup.extensions.Extensions.toUserEntity
import com.testmintyn.binlookup.model.User
import com.testmintyn.binlookup.repository.UserRepository
import com.testmintyn.binlookup.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.Objects

@Service
class UserServiceImpl(private val userRepository: UserRepository, private val passwordEncoder: BCryptPasswordEncoder) :
    UserService, UserDetailsService {
    override fun createUser(userDto: UserDto): User {
        try {
            //hash the user password, don't persist clear password in the storage
            userDto.password = passwordEncoder.encode(userDto.password)
            return userRepository.save(userDto.toUserEntity())
        } catch (e: DataIntegrityViolationException) {
            throw UserCreationException(
                "${e.cause?.message} :: DataIntegrityViolationException"
            )
        } catch (e: Exception) {
            throw UserCreationException("User creation error")
        }
    }

    override fun getUserByEmail(email: String): User {
        val user = userRepository.findByEmail(email)
        if (Objects.isNull(user)) {
            throw NotFoundException("User with email $email not found")
        }
        return user!!
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        val email = username ?: ""
        if (email.isEmpty()) {
            throw GeneralException("Invalid username or email")
        }
        return getUserByEmail(email).toUserDetails()
    }
}