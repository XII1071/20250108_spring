package com.example.apiserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class RestControllerExceptionAdvice {
  @ExceptionHandler
  public ResponseEntity handleException(Exception e) {
    // 에러 원인에 대한 로깅 추가
    return new ResponseEntity<>(new ErrorResponse("Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    // 500
  }

  @ExceptionHandler
  public ResponseEntity handleException(BadRequestException e) {
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    // 400
  }

  @ExceptionHandler
  public ResponseEntity handleException(UnauthenticatedException e) {
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    // 401
  }

  @ExceptionHandler
  public ResponseEntity handleException(ForbiddenException e) {
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    // 403
  }

  @ExceptionHandler
  public ResponseEntity handleException(NoResourceFoundException e) {
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    // 404
  }

  static class ErrorResponse {
    String message;

    ErrorResponse(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }

  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
  private class BadRequestException extends RuntimeException {
  }

  @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "잘못된 요청 오류")
  private class UnauthenticatedException extends RuntimeException {
  }

  @ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "잘못된 요청 오류")
  private class ForbiddenException extends RuntimeException {
  }
}