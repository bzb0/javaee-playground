package com.bzb.javaee.service.executor;

import com.bzb.javaee.model.ComputationFinished;
import com.bzb.javaee.qualifier.ContainerExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * A simple task executor, which uses the container managed {@link ManagedExecutorService} to execute the tasks.
 */
@ApplicationScoped
public class SimpleExecutor {

  @Inject
  private Event<ComputationFinished> computationFinishedEvent;
  @Inject
  @ContainerExecutorService
  private ManagedExecutorService executorService;

  /**
   * Executes the provided {@link Runnable}.
   *
   * @param job The job to be executed.
   */
  public void executeJob(Runnable job) {
    execute(job);
  }

  /**
   * Prints the execution time of a finished task.
   *
   * @param finished The finished computation event.
   */
  public void onComputationFinished(@Observes ComputationFinished finished) {
    System.out.println("Execution finished at: " + finished.getFinishTimeMillis());
  }

  private void execute(Runnable runnable) {
    Future<?> future = executorService.submit(runnable);
    try {
      future.get();
      computationFinishedEvent.fire(new ComputationFinished(System.currentTimeMillis()));
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }
}
