package pico.erp.shared.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import pico.erp.shared.event.Event;
import pico.erp.shared.event.EventPublisher;

@Slf4j
public class JmsEventPublisher implements EventPublisher {

  @Lazy
  @Autowired
  private JmsTemplate jmsTemplate;

  @Override
  public void publishEvent(Event event) {
    if (TransactionSynchronizationManager.isSynchronizationActive()) {
      TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            if (log.isDebugEnabled()) {
              log.debug("jms event fired {}", event.channel());
            }
            jmsTemplate.convertAndSend(new ActiveMQTopic(event.channel()), event);
          }

        });
    } else {
      if (log.isDebugEnabled()) {
        log.debug("jms event fired {}", event.channel());
      }
      jmsTemplate.convertAndSend(new ActiveMQTopic(event.channel()), event);
    }

  }

}
