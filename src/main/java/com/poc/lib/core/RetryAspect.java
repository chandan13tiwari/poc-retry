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

    private final RetryServiceImpl retryService;

    public RetryAspect(RetryServiceImpl retryService) {
        this.retryService = retryService;
    }

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

        if(numberOfRetries == 0) {
            throw new RetryException("Number of Retries can't be 0, please provide correct values");
        }

        if(numberOfRetries < 0) {
            throw new RetryException("Number of Retries can't be negative, please provide correct values");
        }

        if(initialDelay < 0 || retryInterval < 0) {
            throw new RetryException("Time can't be negative, please provide correct values");
        }

        for(int i=0; i<numberOfRetries; i++) {
            try{
                if(i == 0) {
                    retryService.pollDelayForRetry(initialDelay, initialDelayTimeUnit);
                }

                if(i == numberOfRetries - 1) {
                    log.info("All {} retries are exhausted! This might happen because some services are not working as expected.", numberOfRetries);
                }

                interval = interval * multiplier;
                retryService.pollIntervalForRetry(interval, intervalTimeUnit);
                result = pjp.proceed();
            } catch (Exception ex) {
                log.error("Exception occurred while retrying: {}", ex.getMessage());
            }
        }

        return result;
    }
}
