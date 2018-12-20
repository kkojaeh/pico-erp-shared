package pico.erp.shared;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import lombok.val;

public interface ApplicationStarter {

  static void sort(List<ApplicationStarter> starters) {
    val orders = new HashMap<ApplicationId, Integer>();
    starters.forEach(starter -> {
      orders.put(starter.getId(), 0);
    });
    starters.forEach(starter -> {
      val order = starter.getDependencies().stream()
        .map(id -> orders.get(id))
        .max(Comparator.comparing(i -> i))
        .orElse(0) + 1;
      orders.put(starter.getId(), order);
    });
    starters.sort(Comparator.comparing(starter -> orders.get(starter.getId())));
  }

  Set<ApplicationId> getDependencies();

  ApplicationId getId();

  boolean isWeb();

  Application start(String... args);

}
