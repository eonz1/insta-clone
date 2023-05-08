package com.cgram.prom.global.base.exception.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/*
 * {
 *   "code": "404",
 *   "message": "error_message",
 *   "validation": {
 *       "error_key": ["error_description"],
 *   }
 * }
 */
@Getter
public class ErrorResponse {

    private final int code;
    private final String message;
    private final Map<String, List<String>> validation;

    @Builder
    public ErrorResponse(int code, String message, Map<String, List<String>> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }
}