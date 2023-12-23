package com.testmintyn.binlookup.dto

data class BinResponseData(
    val bank: Bank,
    val brand: String,
    val country: Country,
    val number: Number,
    val prepaid: Boolean,
    val scheme: String,
    val type: String
)

