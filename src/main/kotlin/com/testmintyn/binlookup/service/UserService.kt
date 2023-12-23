package com.testmintyn.binlookup.service

import com.testmintyn.binlookup.model.User

interface UserService {
    fun createUser(user: User) : User

    fun getUserByEmail(email: String) : User
}