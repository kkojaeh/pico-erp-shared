package pico.erp.shared;

public interface ApplicationStarter extends Comparable<ApplicationStarter> {

  @Override
  default int compareTo(ApplicationStarter starter) {
    return (this.getOrder() < starter.getOrder() ? -1 :
      (this.getOrder() == starter.getOrder() ? 0 : 1));
  }

  default int getOrder() {
    return 0;
  }

  boolean isWeb();

  Application start(String... args);

}
