package com.testmintyn.binlookup.service

import com.testmintyn.binlookup.dto.UserDto
import com.testmintyn.binlookup.model.User

interface UserService {
    fun createUser(userDto: UserDto) : User

    fun getUserByEmail(email: String) : User
}