package com.bzb.javaee.mdb;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

/**
 * Stateless EJB that provides functionalities for sending/publishing messages to queues and topics.
 */
@Stateless
@Asynchronous
public class MessagePublisher {

  @Resource(mappedName = "java:/ConnectionFactory")
  private ConnectionFactory connectionFactory;

  @Resource(mappedName = "java:/topic/mdbTextTopic")
  private Topic topic;

  @Resource(mappedName = "java:/queue/mdbTextQueue")
  private Queue queue;

  public void sendMessage(TextMessage msg) {
    try (QueueConnection connection = ((QueueConnectionFactory) connectionFactory).createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE)) {

      QueueSender sender = session.createSender(queue);
      sender.send(msg);
      sender.close();
    } catch (JMSException e) {
      System.err.printf("Error sending message to queue: %s. Message: %s.", e.getMessage(), msg);
    }
  }

  public void publishMessage(TextMessage msg, String correlationID) {
    try (TopicConnection connection = ((TopicConnectionFactory) connectionFactory).createTopicConnection();
        TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE)) {
      TopicPublisher publisher = session.createPublisher(topic);
      msg.setJMSCorrelationID(correlationID);
      publisher.publish(msg);
      publisher.close();
    } catch (JMSException e) {
      System.err.printf("Error publishing message to topic: %s. Message: %s, correlationId: %s.", e.getMessage(), msg, correlationID);
    }
  }
}
