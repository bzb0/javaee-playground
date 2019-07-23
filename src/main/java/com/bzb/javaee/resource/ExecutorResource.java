package com.bzb.javaee.resource;

import com.bzb.javaee.qualifier.BigInteger;
import com.bzb.javaee.qualifier.SmallInteger;
import com.bzb.javaee.service.MathCalculator;
import com.bzb.javaee.service.executor.RetryableExecutor;
import com.bzb.javaee.service.executor.SimpleExecutor;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/executor")
public class ExecutorResource {

  private static final int THREAD_SLEEP_MILLIS = 3000;

  private final ThreadLocalRandom rnd = ThreadLocalRandom.current();

  @Inject
  @SmallInteger
  private int rangeFrom;

  @Inject
  @BigInteger
  private int rangeTo;

  @Inject
  private MathCalculator calculator;

  @Inject
  private RetryableExecutor retryableExecutor;

  @Inject
  private SimpleExecutor simpleExecutor;

  @POST
  @Path("/task")
  public Response executeTask() {
    retryableExecutor.executeWithRetry(() -> {
      Thread.sleep(rnd.nextInt(THREAD_SLEEP_MILLIS));
      if (rnd.nextBoolean()) {
        throw new RuntimeException("Task failed.");
      }
      return "Success";
    });
    return Response.status(Status.NO_CONTENT).build();
  }

  @POST
  @Path("/submission")
  public void create() {
    for (int i = 0; i < 5; i++) {
      simpleExecutor.executeJob(() -> {
        try {
          Thread.sleep(rnd.nextInt(THREAD_SLEEP_MILLIS));
        } catch (InterruptedException e) {
          System.err.println("Thread interrupted: " + e.getMessage());
        }
        System.out.println("Execution finished.");
      });
    }
  }

  @GET
  @Path("/power")
  @Produces(MediaType.TEXT_PLAIN)
  public Number calculatePower(@QueryParam("base") int base, @QueryParam("exponent") int exponent) {
    return calculator.power(base, exponent);
  }

  @GET
  @Path("/range")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Integer> getRange() {
    return IntStream.range(rangeFrom, rangeTo).boxed().collect(Collectors.toList());
  }
}
