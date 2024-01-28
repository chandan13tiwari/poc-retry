package com.github.chandan13tiwari.core;

import com.github.chandan13tiwari.annotation.Retry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetryAspectTest {
  @InjectMocks
  private RetryAspect retryAspect;

  @Mock
  private ProceedingJoinPoint pjp;

  @Mock
  private MethodSignature methodSignature;

  @Mock
  private RetryServiceImpl retryService;

  @Test
  void execute_retryNotRequired_isSuccess() throws Throwable {
    final Method methodSignature = getClass().getDeclaredMethod("retryNotRequiredMethod");
    when(this.methodSignature.getMethod()).thenReturn(methodSignature);
    when(pjp.getSignature()).thenReturn(this.methodSignature);
    when(pjp.proceed()).thenReturn(new Object());

    assertNotNull(retryAspect.executeRetry(pjp));
    verify(pjp, times(1)).proceed();
  }

  @Test
  void execute_retryAspectWithZeroRetries_throwsException() throws Throwable {
    final Method methodSignature = getClass().getDeclaredMethod("zeroNumberOfRetriesMethod");
    when(this.methodSignature.getMethod()).thenReturn(methodSignature);
    when(pjp.getSignature()).thenReturn(this.methodSignature);

    assertThatThrownBy(() -> retryAspect.executeRetry(pjp)).isInstanceOf(RetryException.class)
        .hasMessage("Number of Retries can't be 0, please provide correct values");
  }

  @Test
  void execute_retryAspectWithNegativeRetries_throwsException() throws Throwable {
    final Method methodSignature = getClass().getDeclaredMethod("negativeNumberOfRetriesMethod");
    when(this.methodSignature.getMethod()).thenReturn(methodSignature);
    when(pjp.getSignature()).thenReturn(this.methodSignature);

    assertThatThrownBy(() -> retryAspect.executeRetry(pjp)).isInstanceOf(RetryException.class)
        .hasMessage("Number of Retries can't be negative, please provide correct values");
  }

  @Test
  void execute_retryAspectWithNegativeInitialDelay_throwsException() throws Throwable {
    final Method methodSignature = getClass().getDeclaredMethod("negativeInitialDelayMethod");
    when(this.methodSignature.getMethod()).thenReturn(methodSignature);
    when(pjp.getSignature()).thenReturn(this.methodSignature);

    assertThatThrownBy(() -> retryAspect.executeRetry(pjp)).isInstanceOf(RetryException.class)
        .hasMessage("Time can't be negative, please provide correct values");
  }

  @Test
  void execute_retryAspectWithNegativeRetryInterval_throwsException() throws Throwable {
    final Method methodSignature = getClass().getDeclaredMethod("negativeRetryIntervalMethod");
    when(this.methodSignature.getMethod()).thenReturn(methodSignature);
    when(pjp.getSignature()).thenReturn(this.methodSignature);

    assertThatThrownBy(() -> retryAspect.executeRetry(pjp)).isInstanceOf(RetryException.class)
        .hasMessage("Time can't be negative, please provide correct values");
  }

  @Test
  void execute_whenNumberOfRetriesExhausted_isSuccess() throws Throwable {
    final Method methodSignature = getClass().getDeclaredMethod("retriesExhaustedMethod");
    when(this.methodSignature.getMethod()).thenReturn(methodSignature);
    when(pjp.getSignature()).thenReturn(this.methodSignature);
    when(pjp.proceed()).thenThrow(RuntimeException.class);

    assertNull(retryAspect.executeRetry(pjp));
    verify(pjp, times(3)).proceed();
  }

  @Retry(times = 3)
  public void retriesExhaustedMethod() {
  }

  @Retry(times = 0)
  public void zeroNumberOfRetriesMethod() {
  }

  @Retry(times = -2)
  public void negativeNumberOfRetriesMethod() {
  }

  @Retry(initialDelay = -2)
  public void negativeInitialDelayMethod() {
  }

  @Retry(retryInterval = -2)
  public void negativeRetryIntervalMethod() {
  }

  @Retry
  public void retryNotRequiredMethod() {
  }

}
