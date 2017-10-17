package com.bzb.javaee.resource;

import com.bzb.javaee.service.RandomNumberGenerator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/random")
public class RandomNumberResource {

  @EJB
  private RandomNumberGenerator rndGenerator;

  @GET
  @Path("/integers")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Integer> getNextInt() {
    List<Future<Integer>> rndFutures = IntStream.range(1, 101)
        .mapToObj(i -> rndGenerator.getRandomNumber())
        .collect(Collectors.toList());

    return rndFutures.stream().map(future -> {
      try {
        return future.get();
      } catch (InterruptedException | ExecutionException e) {
        throw new EJBException("Problem occurred in random number generator.", e);
      }
    }).collect(Collectors.toList());
  }
}
