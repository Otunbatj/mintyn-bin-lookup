package com.testmintyn.binlookup.extensions

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.testmintyn.binlookup.dto.ApiResponse
import com.testmintyn.binlookup.dto.BinResponse
import com.testmintyn.binlookup.dto.BinResponseData
import com.testmintyn.binlookup.dto.UserDto
import com.testmintyn.binlookup.model.User
import org.springframework.security.core.userdetails.UserDetails


object Extensions {
    fun UserDto.toUserEntity(): User {
        return User(
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            password = this.password
        )
    }

    /**
     * Json Representation of the User DTO with a little hack to remove
     * user password
     */
    fun User.toJsonString(): Map<String?, Any?>? {
        val objectMapper = ObjectMapper()
        val map = objectMapper.convertValue(this, object : TypeReference<Map<String?, Any?>?>() {})
        return map?.filter {
            it.key?.contains("password") == false
        }
    }

    fun User.toUserDetails(): UserDetails {
        return org.springframework.security.core.userdetails.User(
            email,
            password,
            listOf()
        )
    }

    fun BinResponseData.toBinResponse(): BinResponse {
        return BinResponse(scheme, type, bank.name)
    }
}