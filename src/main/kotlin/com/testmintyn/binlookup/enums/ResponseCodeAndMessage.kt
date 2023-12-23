package com.testmintyn.binlookup.enums

enum class ResponseCodeAndMessage(val code: String, val message: String) {
    SUCCESS_RESPONSE_CODE("00", "Processed successfully"),
    FAILED_RESPONSE_CODE("99", "Error occurred")
}