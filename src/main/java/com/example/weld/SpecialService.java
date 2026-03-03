package com.example.weld;

import jakarta.inject.*;

import java.lang.annotation.*;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecialService {}
