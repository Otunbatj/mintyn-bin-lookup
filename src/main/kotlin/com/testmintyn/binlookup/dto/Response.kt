package com.testmintyn.binlookup.dto

data class Response<T>(
    val responseCode: String,
    val responseMessage: String,
    val obj: T?
)