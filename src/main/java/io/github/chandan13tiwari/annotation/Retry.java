package io.github.chandan13tiwari.annotation;

import java.lang.annotation.*;
import java.time.temporal.ChronoUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retry {
  /**
   * Maximum number of retries we can trigger for that particular method. It
   * should be a positive integer. Zero(0) or a Negative integer can be provided
   * as maximum number of retries. By default, Maximum number of retries is set to
   * 10, which means in case of any failure, it will trigger the method 10 times
   * to the max.
   */
  int times() default 10;

  /**
   * Time interval between two retries. Time interval could be in any TimeUnit,
   * you just need to set the time as a long value. You need to set the
   * retryInterval TimeUnit by setting {@link Retry#retryIntervalTimeUnit} By
   * default, retryInterval is set to 5 seconds, which means between first retry
   * there is 5 seconds of wait.
   */
  long retryInterval() default 5L;

  /**
   * Initial delay for the first retry. Initial delay could be in any TimeUnit,
   * you just need to set the time as a Long value. You need to set the
   * initialDelay TimeUnit by setting {@link Retry#initialDelayTimeUnit} By
   * default, initialDelay is set to 0 seconds, which means there is no initial
   * delay by default.
   */
  long initialDelay() default 0L;

  /**
   * Multiplier is the algorithm to trigger the next retry exponentially. It
   * should be an integer value. Zero or negative values are not supported. By
   * default, multiplier value is set to 2, which means the time interval between
   * retries are increased exponentially.
   *
   * @see "Ex., retryInterval is 5, then first retry will trigger after
   *      5x2=10seconds, second retry will trigger after 10x2=20seconds, third
   *      retry will trigger after 20x2=40seconds, and so on..."
   */
  int multiplier() default 2;

  /**
   * TimeUnit for Retry Interval
   */
  ChronoUnit retryIntervalTimeUnit() default ChronoUnit.SECONDS;

  /**
   * TimeUnit for Initial Delay
   */
  ChronoUnit initialDelayTimeUnit() default ChronoUnit.SECONDS;
}
