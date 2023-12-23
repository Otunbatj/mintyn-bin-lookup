package com.testmintyn.binlookup.dto
 class StatsResponse(
    start: Int,
    limit: Int,
    size: Int,
    payloadData: Map<String, Int>
) : ApiResponse(true, StatsPayload(start, limit, size, payloadData))
