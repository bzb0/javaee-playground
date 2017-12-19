package com.bzb.javaee.resource;

import com.bzb.javaee.mdb.MessagePublisher;
import java.util.UUID;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/topic")
public class MessagePublisherResource {

  private final static String MESSAGE_PARAM = "msg";

  @Inject
  private MessagePublisher messagePublisher;

  @Inject
  private JMSContext jmsContext;

  @POST
  @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
  @Path("/textMessages")
  public Response publishTextMessage(MultivaluedMap<String, String> formData) {
    String correlationId = UUID.randomUUID().toString();
    messagePublisher.publishMessage(jmsContext.createTextMessage(formData.getFirst(MESSAGE_PARAM)), correlationId);
    return Response.status(Status.NO_CONTENT).build();
  }
}
