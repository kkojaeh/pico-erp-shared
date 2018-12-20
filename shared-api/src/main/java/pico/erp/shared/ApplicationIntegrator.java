package pico.erp.shared;

public interface ApplicationIntegrator {

  ApplicationIntegrator add(Application application);

  void complete();

  <T> T getBean(Class<T> requiredType);

  ApplicationIntegrator integrate();

  ApplicationIntegrator integrateEventPublisher();

  ApplicationIntegrator integrateMessageSource();

  ApplicationIntegrator integrateTransaction();

}
