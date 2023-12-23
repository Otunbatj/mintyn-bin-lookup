package com.testmintyn.binlookup.exception

import com.testmintyn.binlookup.dto.ErrorResponse
import com.testmintyn.binlookup.dto.Response
import com.testmintyn.binlookup.enums.ResponseCodeAndMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import java.util.function.Consumer
import javax.validation.ConstraintViolationException


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        logger.error("MethodArgumentNotValidException")
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.fieldErrors.forEach(Consumer { error: FieldError ->
            errors[error.field] = error.defaultMessage
        })
        val response = Response(
            ResponseCodeAndMessage.FAILED_RESPONSE_CODE.code,
            ResponseCodeAndMessage.FAILED_RESPONSE_CODE.message,
            errors
        )
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleConstraintViolationException(
        exception: ConstraintViolationException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val response = Response(
            ResponseCodeAndMessage.FAILED_RESPONSE_CODE.code,
            "One or more constraint has been violated",
            exception.message ?: ""
        )
        return ResponseEntity.status(HttpStatus.valueOf(400)).body(response)
    }

    @ExceptionHandler(value = [UserCreationException::class])
    fun handleUserCreationException(exception: UserCreationException, request: WebRequest): ResponseEntity<Any> {
        val response = Response(
            ResponseCodeAndMessage.FAILED_RESPONSE_CODE.code,
            "Error occurred while creating user",
            exception.message ?: ""
        )
        return ResponseEntity.status(HttpStatus.valueOf(500)).body(response)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(exception: NotFoundException, request: WebRequest): ResponseEntity<Any> {
        val response = Response(ResponseCodeAndMessage.FAILED_RESPONSE_CODE.code, "Not found", exception.message ?: "")
        return ResponseEntity.status(HttpStatus.valueOf(404)).body(response)
    }

    @ExceptionHandler(value = [BinLookupException::class])
    fun handleBinLookupException(exception: BinLookupException, request: WebRequest): ResponseEntity<Any> {
        val response = ErrorResponse(exception.message ?: "")
        return ResponseEntity.status(HttpStatus.valueOf(404)).body(response)
    }
}