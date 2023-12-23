package com.testmintyn.binlookup.controller

import com.testmintyn.binlookup.service.AppService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@RestController
@RequestMapping("/api/v1/card-scheme")
@Validated
class BinLookupController {
    @Autowired
    lateinit var appService: AppService

    @GetMapping("/verify/{bin}")
    fun verifyBin(
        @PathVariable @NotBlank(message = "BIN cannot be empty") @Size(
            min = 6,
            max = 8,
            message = "BIN must be either 6 or 8 characters"
        ) bin: String
    ): ResponseEntity<Any> {
        val response = appService.lookupBIN(bin)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/stats")
    fun getBinHitStat(
        @RequestParam(defaultValue = "1") start: Int,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<Any> {
        val statData = appService.getTotalBinHit(start, limit)
        return ResponseEntity.ok(statData)
    }
}