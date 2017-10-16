package com.bzb.javaee.service.executor;

import com.bzb.javaee.model.ComputationResult;
import com.bzb.javaee.model.FailedComputation;
import com.bzb.javaee.qualifier.ContainerExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * A simple task executor with retry semantics. A failed task will be retried {@link RetryableExecutor#MAX_RETRIES} times.
 */
@ApplicationScoped
public class RetryableExecutor {

  private static final int MAX_RETRIES = 3;

  @Inject
  private Event<ComputationResult> computationResultEvent;

  @Inject
  private Event<FailedComputation> failedComputationEvent;

  @Inject
  @ContainerExecutorService
  private ManagedExecutorService executorService;

  /**
   * Executes the provided {@link Callable}. The executor will retry a failed computation {@link RetryableExecutor#MAX_RETRIES} times.
   *
   * @param callable The task/computation to be executed.
   * @param <T>      The computation result type.
   */
  public <T> void executeWithRetry(Callable<T> callable) {
    executeComputation(callable, 0);
  }

  /**
   * Executes the provided {@link Callable}. The executor will retry a failed computation {@link RetryableExecutor#MAX_RETRIES} times.
   *
   * @param callable   The task/computation to be executed.
   * @param numRetries The number of retries.
   * @param <T>        The computation result type.
   */
  <T> void executeWithRetry(Callable<T> callable, int numRetries) {
    /* We have reached the max. number of retries. */
    if (numRetries >= MAX_RETRIES) {
      System.out.println("Max. number of retries reached.");
      return;
    }
    executeComputation(callable, numRetries);
  }

  /**
   * Executes the provided task/computation.
   *
   * @param callable   The task/computation to be executed.
   * @param numRetries Number of task retries.
   * @param <T>        The computation result type.
   */
  private <T> void executeComputation(Callable<T> callable, int numRetries) {
    try {
      long start = System.currentTimeMillis();
      Future<T> asyncResult = executorService.submit(callable);
      T result = asyncResult.get();
      long durationMillis = System.currentTimeMillis() - start;
      computationResultEvent.fire(new ComputationResult(result, durationMillis, numRetries, callable));
    } catch (InterruptedException | ExecutionException e) {
      System.err.println(e.getMessage());
      failedComputationEvent.fire(new FailedComputation(callable, numRetries));
    }
  }
}
