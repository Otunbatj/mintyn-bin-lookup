package com.testmintyn.binlookup.dto

sealed class ApiResponse(
    val success: Boolean,
    val payload: Payload
)
