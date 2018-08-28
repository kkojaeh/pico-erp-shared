package pico.erp.shared;

import java.lang.annotation.Annotation;
import java.util.Set;
import lombok.Value;
import pico.erp.shared.event.EventPublisher;

public interface Application {

  void alterEventPublisher(Object eventPublisher);

  void alterMessageSource(Object messageSource);

  void alterTransactionManager(Object transactionManager);

  void complete();

  <T extends Annotation> Set<T> getAnnotates(Class<T> annotationClass);

  <T> T getBean(Class<T> requiredType);

  EventPublisher getEventPublisher();

  Object getMessageSource();

  String getName();

  Set<ApplicationBean> getPublicBeans();

  Object getTransactionManager();

  boolean hasBean(Class<?> requiredType);

  void registerBeans(Set<ApplicationBean> beans);

  @Value
  class ApplicationBean {

    String name;

    Object bean;
  }

}
