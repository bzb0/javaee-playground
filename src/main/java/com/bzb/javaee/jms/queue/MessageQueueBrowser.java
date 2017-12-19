package com.bzb.javaee.jms.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.TextMessage;

/**
 * A wrapper bean for a {@link QueueBrowser}.
 */
@ApplicationScoped
public class MessageQueueBrowser {

  @Inject
  private JMSContext jmsContext;

  /**
   * Returns a list of unconsumed text messages in the specified queue.
   *
   * @param queueName The queue name.
   * @return List of unread text messages.
   */
  public List<String> getMessages(String queueName) {
    /* Creating a queue browser and checking for the messages on the queue. */
    QueueBrowser browser = jmsContext.createBrowser(jmsContext.createQueue(queueName));

    try {
      return getTextMessages(browser.getEnumeration());
    } catch (JMSException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  private List<String> getTextMessages(Enumeration<?> elements) {
    List<String> messages = new ArrayList<>();
    while (elements.hasMoreElements()) {
      Object nextElement = elements.nextElement();
      getText(nextElement).ifPresent(messages::add);
    }
    return messages;
  }

  private Optional<String> getText(Object message) {
    if (!(message instanceof TextMessage)) {
      return Optional.empty();
    }

    try {
      return Optional.ofNullable(((TextMessage) message).getText());
    } catch (JMSException e) {
      return Optional.empty();
    }
  }
}
