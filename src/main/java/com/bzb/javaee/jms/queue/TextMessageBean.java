package com.bzb.javaee.jms.queue;

import java.util.Optional;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

@RequestScoped
public class TextMessageBean {

  public final static String QUEUE_NAME = "textQueue";
  private final static int TIMEOUT_MILLIS = 1500;

  @Inject
  private JMSContext context;

  @Resource(lookup = "java:/queue/" + QUEUE_NAME)
  private Queue queue;

  /**
   * Sends a text message to the queue (textQueue).
   *
   * @param message The text message to be sent.
   */
  public void sendMessage(String message) {
    try {
      context.createProducer().send(queue, message);
    } catch (JMSRuntimeException t) {
      t.printStackTrace();
    }
  }

  /**
   * Receives/fetches the next message from the queue (textQueue).
   *
   * @return The received message from the queue, or {@link Optional#empty()} if the queue is empty.
   */
  public Optional<String> getNextMessage() {
    try {
      JMSConsumer receiver = context.createConsumer(queue);
      String text = receiver.receiveBody(String.class, TIMEOUT_MILLIS);
      return Optional.ofNullable(text);
    } catch (JMSRuntimeException t) {
      t.printStackTrace();
      return Optional.empty();
    }
  }
}
