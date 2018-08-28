package pico.erp.shared.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import lombok.AllArgsConstructor;
import org.apache.activemq.ScheduledMessage;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * https://dzone.com/articles/sending-delayed-jms-messages
 */
@AllArgsConstructor
public class AMQDelayedMessageConverter implements MessageConverter {

  private final MessageConverter messageConverter;

  @Override
  public Object fromMessage(Message message) throws JMSException, MessageConversionException {
    return messageConverter.fromMessage(message);
  }

  @Override
  public Message toMessage(Object object, Session session)
    throws JMSException, MessageConversionException {
    Message message = messageConverter.toMessage(object, session);
    message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1);
    return message;
  }
}
