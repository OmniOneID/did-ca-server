/*
 * Copyright 2024 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omnione.did.base.controller;

import lombok.RequiredArgsConstructor;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.base.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * The GlobalControllerAdvice class provides centralized exception handling across all controllers
 * within the specified base package. It catches and processes various types of exceptions that may occur
 * during the execution of controller methods, ensuring consistent error responses and smooth application flow.
 */
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = {"org.omnione.did"})
public class GlobalControllerAdvice {

    /**
     * Handles OpenDidException that occurs during the execution of controller methods.
     * If an ErrorResponse is present in the exception, it returns that response with a status code of 400.
     * Otherwise, it constructs a new ErrorResponse based on the ErrorCode and returns it with the appropriate HTTP status.
     *
     * @param ex the OpenDidException that was thrown
     * @return a ResponseEntity containing the error response and the HTTP status code
     */
    @ExceptionHandler(OpenDidException.class)
    public ResponseEntity<ErrorResponse> handleVerifierException(OpenDidException ex) {
        int httpStatus = ex.getErrorCode().getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getCode(), ex.getErrorCode().getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(httpStatus));
    }

    /**
     * Handles MethodArgumentNotValidException that occurs during the execution of controller methods.
     * This exception is typically thrown when a method argument annotated with @Valid fails validation.
     * It collects all validation error messages and returns them in a single ErrorResponse with a 500 status code.
     *
     * @param ex the MethodArgumentNotValidException that was thrown
     * @return a ResponseEntity containing the error response and a 500 HTTP status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        int httpStatus = 400;

        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse errorResponse = new ErrorResponse("9999", errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(httpStatus));
    }

    /**
     * Handles HttpMessageNotReadableException that occurs when a request body is not readable or is invalid.
     * This exception typically occurs when the incoming JSON is malformed or missing required fields.
     * It returns an ErrorResponse with a 500 status code indicating a bad request.
     *
     * @param ex the HttpMessageNotReadableException that was thrown
     * @return a ResponseEntity containing the error response and a 500 HTTP status code
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(HttpMessageNotReadableException ex) {
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.REQUEST_BODY_UNREADABLE);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(500));
    }
}
