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

package org.omnione.did.base.exception;

import org.omnione.did.base.response.ErrorResponse;
import lombok.Getter;

/**
 * Custom exception class for OpenDID-related errors.
 * This exception encapsulates an ErrorCode to provide more detailed error information.
 *
 */
@Getter
public class OpenDidException extends RuntimeException{
    private ErrorCode errorCode;
    private ErrorResponse errorResponse;

    /**
     * Constructs a new OpenDidException with the specified error code.
     *
     * @param errorCode The ErrorCode enum value representing the specific error.
     */
    public OpenDidException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

   /**
     * Constructs a new OpenDidException with the specified error response.
     *
     * @param errorResponse The ErrorResponse object representing the specific error.
     */
    public OpenDidException(ErrorResponse errorResponse) {
        super(errorResponse.getDescription());
        this.errorResponse = errorResponse;
    }
}
