package com.poc.retry.core;

/**
 * Thrown when there is a failure or violation of input parameters of
 * {@link com.poc.retry.annotation.Retry} annotation.
 */
public class RetryException extends RuntimeException {

  public RetryException(String message) {
    super(message);
  }

  public RetryException(String message, Throwable cause) {
    super(message, cause);
  }

  public RetryException(Throwable cause) {
    super(cause);
  }
}
