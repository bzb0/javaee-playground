package com.bzb.javaee.resource;

import com.bzb.javaee.jms.queue.MessageQueueBrowser;
import com.bzb.javaee.jms.queue.TextMessageBean;
import com.bzb.javaee.mdb.MessagePublisher;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/queue")
public class MessageQueueResource {

  private final static String MESSAGE_PARAM = "msg";

  @Inject
  private TextMessageBean messageBean;

  @Inject
  private MessageQueueBrowser queueBrowser;

  @Inject
  private MessagePublisher messagePublisher;

  @Inject
  private JMSContext jmsContext;

  @POST
  @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
  @Path("/textMessages")
  public Response sendTextMessage(MultivaluedMap<String, String> formData) {
    messagePublisher.sendMessage(jmsContext.createTextMessage(formData.getFirst(MESSAGE_PARAM)));
    return Response.status(Status.NO_CONTENT).build();
  }

  @POST
  @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
  @Path("/messages")
  public Response sendMessage(MultivaluedMap<String, String> formData) {
    messageBean.sendMessage(formData.getFirst(MESSAGE_PARAM));
    return Response.status(Status.NO_CONTENT).build();
  }

  @GET
  @Path("/messages")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getNextMessage() {
    Optional<String> msg = messageBean.getNextMessage();
    return msg.map(t -> Response.ok(t).build()).orElse(Response.status(Status.NOT_FOUND).build());
  }

  /**
   * Returns a list of unconsumed messages, namely messages that are still in the queue.
   *
   * @return List of unconsumed messages.
   */
  @GET
  @Path("/unconsumedMessages")
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> getMessages() {
    return queueBrowser.getMessages(TextMessageBean.QUEUE_NAME);
  }
}
