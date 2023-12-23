package com.testmintyn.binlookup.dto

data class ErrorResponse(
    var message: String
) : ApiResponse(false, ErrorPayload(message))
