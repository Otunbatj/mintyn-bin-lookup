package com.testmintyn.binlookup.service.impl

import com.testmintyn.binlookup.exception.RemoteServiceException
import com.testmintyn.binlookup.service.HttpService
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class HttpServiceImpl : HttpService {
    val LOGGER = LoggerFactory.getLogger(HttpServiceImpl::class.java)
    override fun post(headerList: Map<String, String>, jsonPayload: String, url: String): HttpResponse<JsonNode> {
        return try {
            LOGGER.info(
                "Making POST request with header {}, jsonPayload {} and url {}",
                headerList,
                jsonPayload,
                url
            )
            //Unirest.config().verifySsl(false);
            Unirest.post(url)
                .headers(headerList)
                .body(jsonPayload)
                .asJson()
        } catch (exception: Exception) {
            LOGGER.info(exception.message)
            throw RemoteServiceException("Remote Service unreachable")
        }
    }

    override fun get(
        headerList: Map<String, String>,
        params: Map<String, Object>,
        url: String
    ): HttpResponse<JsonNode> {
        return try {
            LOGGER.info(
                "Making GET request with header {}, params {} and url {}",
                headerList,
                params,
                url
            )
            val getRequest = Unirest.get(url).headers(headerList)
            if (Objects.isNull(params)) {
                getRequest.asJson()
            } else {
                getRequest.routeParam(params).asJson()
            }
        } catch (exception: java.lang.Exception) {
            LOGGER.info(exception.message)
            throw RemoteServiceException("Remote Service unreachable")
        }
    }

    override fun getForBasicAuth(username: String, password: String, url: String): HttpResponse<JsonNode> {
        return try {
            LOGGER.info("Making GET request using Basic auth username {} and url {}", username, url)
            Unirest.get(url).basicAuth(username, password).asJson()
        } catch (exception: java.lang.Exception) {
            LOGGER.info(exception.message)
            throw RemoteServiceException("Remote Service unreachable")
        }
    }
}