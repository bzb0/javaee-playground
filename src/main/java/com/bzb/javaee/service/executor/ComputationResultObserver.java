package com.bzb.javaee.service.executor;

import com.bzb.javaee.model.ComputationResult;
import com.bzb.javaee.model.FailedComputation;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * An observer for a {@link ComputationResult} events.
 */
@ApplicationScoped
public class ComputationResultObserver {

  private final RetryableExecutor retryableExecutor;

  @Inject
  public ComputationResultObserver(RetryableExecutor retryableExecutor) {
    this.retryableExecutor = retryableExecutor;
  }

  /**
   * Prints the computation result on the console.
   *
   * @param result The computation result event.
   */
  public void onSuccess(@Observes ComputationResult result) {
    System.out.printf("Execution finished: %s.", result);
  }

  /**
   * Retries a failed computation result.
   *
   * @param result The computation result event.
   */
  public void onFailure(@Observes FailedComputation result) {
    int numRetries = result.getNumRetries() + 1;
    System.out.printf("Retrying execution #%d.%n", numRetries);
    result.setNumRetries(numRetries);
    retryableExecutor.executeWithRetry(result.getComputation(), numRetries);
  }
}
