package com.testmintyn.binlookup.service

import com.testmintyn.binlookup.dto.ApiResponse
import com.testmintyn.binlookup.dto.Response
import com.testmintyn.binlookup.dto.UserDto

interface AppService {
    fun createUser(userDto: UserDto) : Response<Any>

    fun getUser(email: String) : Response<Any>

    fun lookupBIN(bin: String) : ApiResponse?

    fun getTotalBinHit(start: Int, limit: Int) : ApiResponse
}