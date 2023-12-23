package com.testmintyn.binlookup.dto

class BinResponse(
    scheme: String?,
    type: String?,
    bank: String?
) : ApiResponse(true, BinPayload(scheme, type, bank))