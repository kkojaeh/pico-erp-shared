package pico.erp.shared.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pico.erp.shared.event.Event;
import pico.erp.shared.event.EventPublisher;

@AllArgsConstructor
public class DelegateEventPublisher implements EventPublisher {

  @Getter
  @Setter
  private EventPublisher delegate;

  @Override
  public void publishEvent(Event event) {
    delegate.publishEvent(event);
  }

}
