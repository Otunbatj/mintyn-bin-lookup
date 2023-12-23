package com.testmintyn.binlookup

import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import java.util.function.Consumer


object Utility {
    const val BIN_BASE_URL = "https://lookup.binlist.net/"
    const val BIN_RESPONSE_CACHE_NAME = "bin-responses"
}