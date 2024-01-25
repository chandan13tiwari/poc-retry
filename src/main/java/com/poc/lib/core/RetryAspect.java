package com.poc.lib.core;

import com.poc.lib.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class RetryAspect {
    @Around("@annotation(com.poc.lib.annotation.Retry)")
    public Object executeRetry(ProceedingJoinPoint pjp) throws Throwable {
        final Retry retryOperation = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(Retry.class);
        final int numberOfRetries = retryOperation.times();
        final long retryInterval = retryOperation.retryInterval();
        final long initialDelay = retryOperation.initialDelay();
        final int multiplier = retryOperation.multiplier();
        final ChronoUnit intervalTimeUnit = retryOperation.retryIntervalTimeUnit();
        final ChronoUnit initialDelayTimeUnit = retryOperation.initialDelayTimeUnit();

        Object result = null;
        long interval = retryInterval;

        pollDelayForRetry(initialDelay, initialDelayTimeUnit);

        for(int i=0; i<numberOfRetries; i++) {
            try{
                interval = interval * multiplier;
                pollDelayForRetry(interval, intervalTimeUnit);
                result = pjp.proceed();
            } catch (Exception ex) {
                log.error("Exception thrown: {}", ex.getMessage());
            }
        }

        log.info("After logs......");

        return result;
    }

    private static void pollDelayForRetry(long delay, ChronoUnit delayTimeUnit) {
        Awaitility.await()
                .timeout(delay + 1, TimeUnit.of(delayTimeUnit))
                .pollDelay(delay, TimeUnit.of(delayTimeUnit))
                .untilAsserted(() -> Assert.assertTrue(true));
    }
}
