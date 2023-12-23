package com.testmintyn.binlookup.service

import kong.unirest.HttpResponse
import kong.unirest.JsonNode

interface HttpService {
    fun post(headerList: Map<String, String>, jsonPayload: String, url: String) : HttpResponse<JsonNode>

    fun get(headerList: Map<String, String>, params: Map<String, Object>, url: String) : HttpResponse<JsonNode>

    fun getForBasicAuth(username: String, password: String, url: String) : HttpResponse<JsonNode>
}