package com.poc.lib.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retry {
    int times() default 10;
    int interval() default 5000;
    int delay() default 0;
}
