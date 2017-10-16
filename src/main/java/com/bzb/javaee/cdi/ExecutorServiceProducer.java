package com.bzb.javaee.cdi;

import com.bzb.javaee.qualifier.ContainerExecutorService;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * A CDI producer for the container managed {@link ManagedExecutorService}. This class facilitates the usage of the {
 * @link ManagedExecutorService} as a CDI bean.
 */
@ApplicationScoped
public class ExecutorServiceProducer {

  @Resource(name = "comp/DefaultManagedExecutorService")
  private ManagedExecutorService executorService;

  @Produces
  @ContainerExecutorService
  public ManagedExecutorService getExecutorService() {
    return executorService;
  }
}
