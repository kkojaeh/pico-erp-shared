package pico.erp.shared;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.val;

public interface ApplicationStarter {

  static List<ApplicationStarter> sort(List<ApplicationStarter> starters) {
    val result = new LinkedList<ApplicationStarter>();
    val targets = new LinkedList<ApplicationStarter>();
    targets.addAll(starters);

    outer:
    while (!targets.isEmpty()) {
      for (ApplicationStarter target : targets) {
        System.out.println(target.getId().getValue() + " : " + target.getDependencies().stream()
          .map(id -> id.getValue()).collect(
            Collectors.toSet()
          ));
        System.out.println("targets : " + targets.stream()
          .map(t -> t.getId().getValue()).collect(
            Collectors.toSet()
          ));
        val hasDependency = targets.stream()
          .map(ApplicationStarter::getId)
          .filter(id -> target.getDependencies().contains(id))
          .count() > 0;
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
