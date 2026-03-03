package com.example.weld;

import jakarta.annotation.*;
import jakarta.interceptor.*;

import static java.util.Arrays.*;



@Interceptor
@Logged
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    @AroundInvoke
    public Object logMethodEntry(InvocationContext ctx)
            throws Exception {
        System.out.printf("Calling method: %s parameters: %s\n",
                ctx.getMethod().getName(), asList(ctx.getParameters()));
        return ctx.proceed();
    }

}
