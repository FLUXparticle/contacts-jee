package com.example.weld;

import jakarta.interceptor.*;

import java.lang.annotation.*;

@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logged {
}
