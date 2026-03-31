package com.ud.testcontainers.wiremock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GithubExceptionHandler {

    @ExceptionHandler(GithubServiceException.class)
    ResponseEntity<ApiError> handle(GithubServiceException ex) {
        ApiError apiError = new ApiError(ex.getMessage());
        return ResponseEntity.internalServerError().body(apiError);
    }

}
