package com.bzb.javaee.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

/**
 * An in memory key-value store, whose concurrent access is managed by the container.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ConcurrentStore implements Serializable {

  private final Map<String, String> store = new HashMap<>();

  @Lock(LockType.WRITE)
  public void put(String key, String value) {
    store.computeIfAbsent(key, (mapKey) -> value);
  }

  @Lock(LockType.READ)
  public boolean containsKey(String key) {
    return store.containsKey(key);
  }

  @Lock(LockType.READ)
  public String get(String key) {
    return store.getOrDefault(key, "");
  }
}
