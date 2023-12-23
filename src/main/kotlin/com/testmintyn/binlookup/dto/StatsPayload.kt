package com.testmintyn.binlookup.dto

data class StatsPayload(
    val start: Int,
    val limit: Int,
    val size: Int,
    val payloadData: Map<String, Int>
) : Payload
