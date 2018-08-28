package pico.erp.shared.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import pico.erp.shared.event.Event;
import pico.erp.shared.event.EventPublisher;

@AllArgsConstructor
public class EventPublisherImpl implements EventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void publishEvent(Event event) {
    applicationEventPublisher.publishEvent(event);
  }

}
