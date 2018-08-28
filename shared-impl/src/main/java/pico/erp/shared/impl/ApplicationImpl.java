package pico.erp.shared.impl;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.transaction.PlatformTransactionManager;
import pico.erp.shared.Application;
import pico.erp.shared.ApplicationInitializer;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

public class ApplicationImpl implements Application {

  private final ConfigurableApplicationContext application;

  public ApplicationImpl(ConfigurableApplicationContext application) {
    this.application = application;
  }

  public void alterEventPublisher(Object eventPublisher) {
    if (eventPublisher instanceof AbstractBeanDefinition) {
      AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) eventPublisher;
      ((BeanDefinitionRegistry) application)
        .registerBeanDefinition("integrationEventPublisher", beanDefinition);
    } else if (eventPublisher instanceof EventPublisher) {
      if (this.hasBean(DelegateEventPublisher.class)) {
        DelegateEventPublisher delegateEventPublisher = application
          .getBean(DelegateEventPublisher.class);
        delegateEventPublisher.setDelegate((EventPublisher) eventPublisher);
      }
    }
  }

  @Override
  public void alterMessageSource(Object messageSource) {
    if (messageSource instanceof MessageSource) {
      MessageSource parentMessageSource = (MessageSource) messageSource;
      HierarchicalMessageSource hierarchicalMessageSource = application
        .getBean(HierarchicalMessageSource.class);
      hierarchicalMessageSource.setParentMessageSource(parentMessageSource);
    }
  }

  @Override
  public void alterTransactionManager(Object transactionManager) {
    if (transactionManager instanceof AbstractBeanDefinition) {
      AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) transactionManager;
      ((BeanDefinitionRegistry) application)
        .registerBeanDefinition("integrationTransactionManager", beanDefinition);
    }

  }

  @Override
  public void complete() {
    Map<String, ApplicationInitializer> beans = application
      .getBeansOfType(ApplicationInitializer.class);
    beans.values().forEach(ApplicationInitializer::initialize);
  }

  @Override
  public <T extends Annotation> Set<T> getAnnotates(Class<T> annotationClass) {
    return Arrays.stream(application.getBeanNamesForAnnotation(annotationClass))
      .map(beanName -> application.findAnnotationOnBean(beanName, annotationClass))
      .collect(Collectors.toSet());
  }

  @Override
  public <T> T getBean(Class<T> requiredType) {
    return application.getBean(requiredType);
  }

  @Override
  public EventPublisher getEventPublisher() {
    return application.getBean(EventPublisher.class);
  }

  @Override
  public Object getMessageSource() {
    return application.getBean(MessageSource.class);
  }

  @Override
  public String getName() {
    return application.getId();
  }

  @Override
  public Set<ApplicationBean> getPublicBeans() {
    return Stream.concat(
      Stream.of(application.getBeanFactory().getBeanDefinitionNames())
        .filter(beanDefinitionName -> {
          BeanDefinition beanDefinition = application.getBeanFactory()
            .getBeanDefinition(beanDefinitionName);
          if (beanDefinition.getSource() instanceof MethodMetadata) {
            return ((MethodMetadata) beanDefinition.getSource())
              .isAnnotated(Public.class.getName());
          }
          return false;
        }),
      Stream.of(application.getBeanNamesForAnnotation(Public.class))
    ).map(name -> {
      Object bean = application.getBean(name);
      if (!bean.getClass().isEnum()) {
        bean = ProxyFactory.getProxy(new SingletonTargetSource(bean));
      }
      return new ApplicationBean(getName() + "/" + name, bean);
    }).collect(Collectors.toSet());
  }

  @Override
  public Object getTransactionManager() {
    if (application.getBeanNamesForType(PlatformTransactionManager.class).length > 0) {
      return application.getBean(PlatformTransactionManager.class);
    }
    return null;
  }

  @Override
  public boolean hasBean(Class<?> requiredType) {
    try {
      application.getBean(requiredType);
      return true;
    } catch (NoSuchBeanDefinitionException e) {
      return false;
    }
  }

  @Override
  public void registerBeans(Set<ApplicationBean> beans) {
    beans.stream().forEach(
      bean -> application.getBeanFactory().registerSingleton(bean.getName(), bean.getBean()));
  }
}
