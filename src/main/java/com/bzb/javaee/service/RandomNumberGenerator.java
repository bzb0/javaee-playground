package com.bzb.javaee.service;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

/**
 * An EJB stateless bean that enables an asynchronous generation of random numbers.
 */
@Stateless
public class RandomNumberGenerator {

  private final ThreadLocalRandom rnd = ThreadLocalRandom.current();

  @Asynchronous
  public Future<Integer> getRandomNumber() {
    return new AsyncResult<>(rnd.nextInt());
  }
}
