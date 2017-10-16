package com.bzb.javaee.model;

import java.io.Serializable;
import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

/**
 * Value class that holds the result of a computation as well as the duration.
 * <p/>
 * This class can't be parametrized, as CDI will not be able to fire/catch the generated event. One solution to this problem is to define a subclass
 * which binds a certain type:
 * <pre>
 *  public class StringComputationResult extends ComputationResult&lt;String&gt; {}
 * </pre>
 */
@Value
@AllArgsConstructor
@ToString(exclude = "computation")
public class ComputationResult implements Serializable {

  Object result;
  long durationMillis;
  int numRetries;
  Callable<?> computation;
}
