package com.bzb.javaee.mdb;

import java.text.MessageFormat;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * This is a {@link MessageDriven} bean, which listens/observers for new {@link TextMessage} from a queue.
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/queue/mdbTextQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class QueueListener implements MessageListener {

  @Resource
  private MessageDrivenContext mdc;

  @Override
  public void onMessage(Message message) {
    if (!(message instanceof TextMessage)) {
      System.out.println(MessageFormat.format("Ignoring message from queue with type: {0}.", message.getClass().getName()));
    }

    try {
      System.out.println(MessageFormat.format("Received message from queue: {0}.", message.getBody(String.class)));
    } catch (JMSException e) {
      System.err.println(MessageFormat.format("Error receiving message: {0}. Message: {1}.", e.getMessage(), message));
      mdc.setRollbackOnly();
    }
  }
}
