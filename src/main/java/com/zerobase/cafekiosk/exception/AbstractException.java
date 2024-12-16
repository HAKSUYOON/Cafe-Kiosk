package com.zerobase.cafekiosk.exception;

public abstract class AbstractException extends RuntimeException{

  abstract public int getStatusCode();
  abstract public String getMessage();
}
