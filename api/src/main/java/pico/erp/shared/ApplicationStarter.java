package pico.erp.shared;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lombok.val;

public interface ApplicationStarter {

  static List<ApplicationStarter> sort(List<ApplicationStarter> starters) {
    val result = new LinkedList<ApplicationStarter>();
    val targets = new LinkedList<ApplicationStarter>(starters);

    while (!targets.isEmpty()) {
      ApplicationStarter found = null;
      for (ApplicationStarter target : targets) {
        val hasDependency = targets.stream()
          .map(ApplicationStarter::getId)
          .filter(id -> target.getDependencies().contains(id))
          .count() > 0;
        if (!hasDependency) {
          found = target;
          break;
        }
      }
      if (found == null) {
        throw new RuntimeException("Graph has cycles");
      } else {
        targets.remove(found);
        result.add(found);
      }
    }
    return result;
  }

  Set<ApplicationId> getDependencies();

  ApplicationId getId();

  boolean isWeb();

  Application start(String... args);


}
