package com.example.demo;

import me.alidg.errors.annotation.ExceptionMapping;
import me.alidg.errors.annotation.ExposeAsArg;
import org.springframework.http.HttpStatus;

@ExceptionMapping(statusCode = HttpStatus.BAD_REQUEST, errorCode = "resource.not_found")
public class ResourceNotFoundException extends RuntimeException {

  @ExposeAsArg(0)
  private final String name;

  public ResourceNotFoundException(final String message, final String name) {
    super(message);
    this.name = name;
  }
}
