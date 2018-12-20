package pico.erp.shared;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lombok.val;

public interface ApplicationStarter {

  static List<ApplicationStarter> sort(List<ApplicationStarter> starters) {
    val result = new LinkedList<ApplicationStarter>();
    val targets = new LinkedList<ApplicationStarter>(starters);

    outer:
    while (!targets.isEmpty()) {
      for (ApplicationStarter target : targets) {
        val hasDependency = target.getDependencies()
          .stream()
          .anyMatch(dependency -> targets.stream().anyMatch(t -> t.getId().equals(dependency)));
        if (!hasDependency) {
          targets.remove(target);
          result.add(target);
          continue outer;
        }
      }
      throw new RuntimeException("Graph has cycles");
    }
    return result;
  }

  Set<ApplicationId> getDependencies();

  ApplicationId getId();

  boolean isWeb();

  Application start(String... args);


}