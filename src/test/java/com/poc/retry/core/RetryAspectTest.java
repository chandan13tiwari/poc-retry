package com.poc.retry.core;

import com.poc.retry.annotation.Retry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
  void execute_retryAspect_isSuccess() throws Throwable {
    final Method methodSignature = getClass().getDeclaredMethod("correctMethod");
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

    assertThrows(RetryException.class, () -> retryAspect.executeRetry(pjp));
    verify(pjp, times(0)).proceed();
  }

  @Retry(times = 3, initialDelay = 5, retryInterval = 5, multiplier = 3)
  public void correctMethod(){}

  @Retry(times = 0, initialDelay = 5, retryInterval = 5, multiplier = 3)
  public void zeroNumberOfRetriesMethod(){}

}
