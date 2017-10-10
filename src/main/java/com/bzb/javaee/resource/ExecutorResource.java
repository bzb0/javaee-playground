package com.bzb.javaee.resource;

import com.bzb.javaee.qualifier.BigInteger;
import com.bzb.javaee.qualifier.SmallInteger;
import com.bzb.javaee.service.MathCalculator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/executor")
public class ExecutorResource {

  @Inject
  @SmallInteger
  private int rangeFrom;

  @Inject
  @BigInteger
  private int rangeTo;

  @Inject
  private MathCalculator calculator;

  @GET
  @Path("/power")
  @Produces(MediaType.TEXT_PLAIN)
  public Integer calculatePower(@QueryParam("base") int base, @QueryParam("exponent") int exponent) {
    return calculator.power(base, exponent);
  }

  @GET
  @Path("/range")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Integer> getRange() {
    return IntStream.range(rangeFrom, rangeTo).boxed().collect(Collectors.toList());
  }
}
