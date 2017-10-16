package com.bzb.javaee.qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Qualifier;

/**
 * Qualifier that enables the usage of container managed executor service as a CDI bean.
 */
@Qualifier
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContainerExecutorService {
}
