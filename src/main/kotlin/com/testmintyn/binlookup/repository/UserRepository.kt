package com.testmintyn.binlookup.repository

import com.testmintyn.binlookup.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String) : User?

}