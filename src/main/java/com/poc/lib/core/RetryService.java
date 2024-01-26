package com.poc.lib.core;

import java.time.temporal.ChronoUnit;

public interface RetryService {
    void pollDelayForRetry(long delay, ChronoUnit timeUnit);

    void pollIntervalForRetry(long interval, ChronoUnit timeUnit);
}
