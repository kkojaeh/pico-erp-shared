package pico.erp.shared.event;

import java.util.Collection;

public interface EventPublisher {

  void publishEvent(Event event);

  default void publishEvents(Collection<Event> events) {
    events.forEach(this::publishEvent);
  }
}
