package com.bzb.javaee.interceptor;

import com.bzb.javaee.qualifier.Traced;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * A simple {@link Interceptor} that only prints the name of the 'intercepted' method.
 */
@Traced
@Interceptor
public class TracingInterceptor {

  @AroundInvoke
  public Object intercept(InvocationContext ctx) throws Exception {
    System.out.println("Method: " + ctx.getMethod());
    return ctx.proceed();
  }
}
