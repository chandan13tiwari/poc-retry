package com.poc.retry.core;

import java.time.temporal.ChronoUnit;

public interface RetryService {
  /**
   * Wait for the initial delay using {@link org.awaitility.Awaitility}
   * @param delay initial delay time for the first retry.
   * @param timeUnit TimeUnit for the delay time.
   */
  void pollDelayForRetry(long delay, ChronoUnit timeUnit);

  /**
   * Wait for the initial retry interval between first retry and second retry
   * using {@link org.awaitility.Awaitility}
   * @param interval retry time interval between first and second retry.
   * @param timeUnit TimeUnit for the retry interval.
   */
  void pollIntervalForRetry(long interval, ChronoUnit timeUnit);
}
