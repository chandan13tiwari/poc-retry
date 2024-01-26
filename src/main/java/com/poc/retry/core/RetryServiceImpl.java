package com.poc.retry.core;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
public class RetryServiceImpl implements RetryService {
  @Override
  public void pollDelayForRetry(long delay, ChronoUnit timeUnit) {
    Awaitility.await().timeout(delay + 1, TimeUnit.of(timeUnit)).pollDelay(delay, TimeUnit.of(timeUnit))
        .untilAsserted(() -> Assertions.assertTrue(true));
  }

  @Override
  public void pollIntervalForRetry(long interval, ChronoUnit timeUnit) {
    Awaitility.await().timeout(interval + 1, TimeUnit.of(timeUnit)).pollInterval(interval, TimeUnit.of(timeUnit))
        .untilAsserted(() -> Assertions.assertTrue(true));
  }
}
