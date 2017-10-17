package com.bzb.javaee.resource;

import com.bzb.javaee.service.ConcurrentStore;
import java.net.URI;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/store")
public class StoreResource {

  private static final String KEY_PARAMETER = "key";
  private static final String VALUE_PARAMETER = "value";

  @EJB
  private ConcurrentStore store;

  @POST
  @Path("/")
  @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
  @Produces(MediaType.TEXT_PLAIN)
  public Response storeKeyValuePair(MultivaluedMap<String, String> formData) {
    if (!formData.containsKey(KEY_PARAMETER) || !formData.containsKey(VALUE_PARAMETER)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    String key = formData.getFirst(KEY_PARAMETER);
    String value = formData.getFirst(VALUE_PARAMETER);
    store.put(key, value);
    return Response.created(URI.create("/store?key=" + key)).build();
  }

  @GET
  @Path("/")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getValue(@QueryParam("key") String key) {
    if (!store.containsKey(key)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    return Response.ok().entity(store.get(key)).build();
  }
}
