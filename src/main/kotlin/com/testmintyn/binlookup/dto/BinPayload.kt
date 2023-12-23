package com.testmintyn.binlookup.dto

data class BinPayload(
    val scheme: String?,
    val type: String?,
    val bank: String?
) : Payload
