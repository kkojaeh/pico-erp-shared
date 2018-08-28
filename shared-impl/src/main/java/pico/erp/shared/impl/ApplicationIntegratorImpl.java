package pico.erp.shared.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import pico.erp.shared.Application;
import pico.erp.shared.Application.ApplicationBean;
import pico.erp.shared.ApplicationIntegrator;
import pico.erp.shared.event.Event;
import pico.erp.shared.event.EventPublisher;

public class ApplicationIntegratorImpl implements ApplicationIntegrator {

  private final LinkedList<Application> applications = new LinkedList<>();

  @Override
  public ApplicationIntegrator add(Application application) {
    applications.add(application);
    return this;
  }

  @Override
  public void complete() {
    applications.forEach(Application::complete);
  }

  @Override
  public <T> T getBean(Class<T> requiredType) {
    for (Application application : applications) {
      if (application.hasBean(requiredType)) {
        return application.getBean(requiredType);
      }
    }
    return null;
  }

  @Override
  public ApplicationIntegrator integrate() {
    Map<Application, Set<ApplicationBean>> beans = new HashMap<>();
    applications.stream().forEach(application -> {
      beans.put(application, application.getPublicBeans());
    });
    applications.stream().forEach(application -> {
      beans.keySet().stream()
        .filter(app -> !app.equals(application)) // 동일하면 제외
        .map(app -> beans.get(app))
        .forEach(application::registerBeans);
    });
    return this;
  }

  @Override
  public ApplicationIntegrator integrateEventPublisher() {

    Set<EventPublisher> eventPublishers = applications.stream()
      .filter(application -> application.hasBean(EventPublisher.class))
      .map(Application::getEventPublisher)
      .map(eventPublisher -> (DelegateEventPublisher) eventPublisher)
      .map(delegateEventPublisher -> delegateEventPublisher.getDelegate())
      .collect(Collectors.toSet());

    EventPublisher eventPublisher = new BroadcastEventPublisher(eventPublishers);

    applications.stream().forEach(application -> {
      application.alterEventPublisher(eventPublisher);
    });

    return this;
  }

  @Override
  public ApplicationIntegrator integrateMessageSource() {

    ResourceBundleMessageSource integrated = new ResourceBundleMessageSource();
    integrated.setDefaultEncoding("UTF-8");

    integrated.addBasenames(
      applications.stream()
        .map(Application::getMessageSource)
        .filter(messageSource -> messageSource != null
          && messageSource instanceof AbstractResourceBasedMessageSource)
        .map(messageSource -> (AbstractResourceBasedMessageSource) messageSource)
        .flatMap(messageSource -> messageSource.getBasenameSet().stream())
        .toArray(size -> new String[size])
    );
    applications.forEach(
      application -> application.alterMessageSource(integrated));
    return this;
  }

  @Override
  public ApplicationIntegrator integrateTransaction() {
    BeanDefinitionBuilder builder = BeanDefinitionBuilder
      .rootBeanDefinition(ChainedTransactionManager.class);

    builder.addConstructorArgValue(
      applications.stream()
        .map(Application::getTransactionManager)
        .filter(transactionManager -> transactionManager != null
          && transactionManager instanceof PlatformTransactionManager)
        .toArray(size -> new PlatformTransactionManager[size])
    );

    AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
    beanDefinition.setPrimary(true);

    applications.forEach(application -> application.alterTransactionManager(beanDefinition));
    return this;
  }

  @AllArgsConstructor
  private static class BroadcastEventPublisher implements EventPublisher {

    private final Set<EventPublisher> eventPublishers;

    public void publishEvent(Event event) {
      eventPublishers.forEach(eventPublisher -> eventPublisher.publishEvent(event));
    }
  }

}
