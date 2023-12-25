package com.testmintyn.binlookup.service.impl

import com.google.gson.Gson
import com.testmintyn.binlookup.Utility
import com.testmintyn.binlookup.dto.*
import com.testmintyn.binlookup.enums.ResponseCodeAndMessage
import com.testmintyn.binlookup.exception.BinLookupException
import com.testmintyn.binlookup.exception.NotFoundException
import com.testmintyn.binlookup.exception.UserCreationException
import com.testmintyn.binlookup.extensions.Extensions.toBinResponse
import com.testmintyn.binlookup.extensions.Extensions.toJsonString
import com.testmintyn.binlookup.extensions.Extensions.toUserEntity
import com.testmintyn.binlookup.service.AppService
import com.testmintyn.binlookup.service.HttpService
import com.testmintyn.binlookup.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppServiceImpl : AppService {
    @Autowired
    lateinit var hitCounterService: HitCounterService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var httpService: HttpService

    @Autowired
    lateinit var gson: Gson

    @Value("\${bin.base-url}")
    lateinit var baseUrl: String

    override fun createUser(userDto: UserDto): Response<Any> {
        val user = userService.createUser(userDto)

        if (Objects.nonNull(user.userId)) {
            return Response(
                ResponseCodeAndMessage.SUCCESS_RESPONSE_CODE.code,
                ResponseCodeAndMessage.SUCCESS_RESPONSE_CODE.message,
                user.toJsonString()
            )
        }
        throw UserCreationException("An error occurred while creating user")
    }

    override fun getUser(email: String): Response<Any> {
        val user = userService.getUserByEmail(email)
        if (Objects.nonNull(user)) {
            return Response(
                ResponseCodeAndMessage.SUCCESS_RESPONSE_CODE.code,
                ResponseCodeAndMessage.SUCCESS_RESPONSE_CODE.message,
                user.toJsonString()
            )
        }
        throw NotFoundException("User with email $email not found")
    }

    //Cache the API call so we don't keep sending the same BIN to third party API
    //Also make sure that cache only happens when result is successful
    @Cacheable(value = [Utility.BIN_RESPONSE_CACHE_NAME], key = "#bin", unless = "#result == null || !#result.success")
    override fun lookupBIN(bin: String): ApiResponse? {
        try {
            var header = mutableMapOf(
                Pair("Accept-Version", "3")
            )
            val url = "$baseUrl$bin"
            val responseData = httpService.get(header, mutableMapOf(), url)
            if (responseData.isSuccess) {
                val binResponseString = responseData.body.toString()
                val binResponseData = gson.fromJson(binResponseString, BinResponseData::class.java)
                if (Objects.isNull(binResponseData.bank.name)) {
                    throw NotFoundException("BIN Record for $bin Not Found")
                }
                hitCounterService.incrementCounter(bin)
                return binResponseData.toBinResponse()
            }
            throw BinLookupException(responseData.statusText)
        } catch (e: Exception) {
            //Global Exception handler should handle this
        }
        return null
    }

    override fun getTotalBinHit(start: Int, limit: Int): ApiResponse {
        val paginatedData = hitCounterService.getPaginatedBINStarts(start, limit)
        return StatsResponse(start, limit, hitCounterService.getTotalSize(), paginatedData)
    }
}