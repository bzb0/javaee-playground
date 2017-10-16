package com.bzb.javaee.model;

import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Value class that holds data for a failed computation.
 */
@Getter
@Setter
@ToString(exclude = "computation")
@AllArgsConstructor
public class FailedComputation {

  private Callable<?> computation;
  private int numRetries;
}
