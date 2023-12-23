package com.testmintyn.binlookup.service.impl

import com.testmintyn.binlookup.exception.NotFoundException
import com.testmintyn.binlookup.extensions.Extensions.toUserDetails
import com.testmintyn.binlookup.model.User
import com.testmintyn.binlookup.repository.UserRepository
import com.testmintyn.binlookup.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.Objects

@Service
class UserServiceImpl : UserService, UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder

    override fun createUser(user: User): User {
        //hash the user password, don't persist clear password in the storage
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user)
    }

    override fun getUserByEmail(email: String): User {
        val user = userRepository.findByEmail(email)
        if(Objects.isNull(user)){
            throw NotFoundException("User with email $email not found")
        }
        return user!!
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        val email = username ?: ""
        return getUserByEmail(email).toUserDetails()
    }
}