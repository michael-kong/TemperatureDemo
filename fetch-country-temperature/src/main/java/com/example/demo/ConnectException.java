package com.example.demo;

import me.alidg.errors.annotation.ExceptionMapping;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.BAD_REQUEST, errorCode = "connect.failed")
public class ConnectException extends RuntimeException {

  public ConnectException(final String message) {
    super(message);
  }
}
