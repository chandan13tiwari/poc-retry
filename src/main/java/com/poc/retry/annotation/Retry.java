package com.poc.retry.annotation;

import java.lang.annotation.*;
import java.time.temporal.ChronoUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retry {
  int times() default 10;
  long retryInterval() default 5L;
  long initialDelay() default 0L;
  int multiplier() default 2;
  ChronoUnit retryIntervalTimeUnit() default ChronoUnit.SECONDS;
  ChronoUnit initialDelayTimeUnit() default ChronoUnit.SECONDS;
}
