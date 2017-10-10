package com.bzb.javaee.cdi;

import com.bzb.javaee.qualifier.BigInteger;
import com.bzb.javaee.qualifier.SmallInteger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * An CDI producer for {@link SmallInteger} and {@link BigInteger} integers.
 */
@ApplicationScoped
public class IntegerProducer {

  @Produces
  @BigInteger
  public Integer createBigInteger(InjectionPoint ip) {
    return 10_000;
  }

  @Produces
  @SmallInteger
  public Integer createSmallInteger(InjectionPoint ip) {
    return 100;
  }
}
