package pico.erp.shared;

import java.util.HashSet;
import java.util.Set;
import kkojaeh.spring.boot.component.SpringBootComponent;
import kkojaeh.spring.boot.component.SpringBootComponentParentReadyEvent;
import lombok.val;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import pico.erp.shared.event.Event;
import pico.erp.shared.event.EventPublisher;

@SpringBootComponent("parent/test")
@SpringBootApplication(exclude = {
  DataSourceAutoConfiguration.class,
  JpaRepositoriesAutoConfiguration.class,
  HibernateJpaAutoConfiguration.class,
  DataSourceTransactionManagerAutoConfiguration.class,
  TaskExecutionAutoConfiguration.class,
  TaskSchedulingAutoConfiguration.class})
@ComponentScan(useDefaultFilters = false)
public class TestParentApplication implements
  ApplicationListener<SpringBootComponentParentReadyEvent> {

  public static void main(String... args) {
    SpringApplication.run(TestParentApplication.class, args);
  }

  @Bean
  public EventPublisher testBroadCastEventPublisher() {
    return new SpringApplicationBroadcastEventPublisher();
  }

  private void eventPublisher(ConfigurableApplicationContext parent,
    Set<ConfigurableApplicationContext> components) {
    val publisher = parent.getBean(SpringApplicationBroadcastEventPublisher.class);
    publisher.publishers.addAll(components);
  }

  private void messageSource(ConfigurableApplicationContext parent,
    Set<ConfigurableApplicationContext> components) {
    val basenames = components.stream()
      .filter(
        component -> component.getBeanNamesForType(AbstractResourceBasedMessageSource.class).length
          > 0)
      .map(component -> component.getBean(AbstractResourceBasedMessageSource.class))
      .flatMap(messageSource -> messageSource.getBasenameSet().stream())
      .toArray(size -> new String[size]);

    val messageSource = parent.getBean(AbstractResourceBasedMessageSource.class);
    messageSource.addBasenames(basenames);
  }

  @Override
  public void onApplicationEvent(SpringBootComponentParentReadyEvent event) {
    transactionManager(event.getParent(), event.getComponents());
    messageSource(event.getParent(), event.getComponents());
    eventPublisher(event.getParent(), event.getComponents());
  }

  private void transactionManager(ConfigurableApplicationContext parent,
    Set<ConfigurableApplicationContext> components) {
    val transactionManagers = components.stream()
      .filter(
        component -> component.getBeanNamesForType(PlatformTransactionManager.class).length > 0)
      .map(component -> component.getBean(PlatformTransactionManager.class))
      .toArray(size -> new PlatformTransactionManager[size]);

    val builder = BeanDefinitionBuilder.genericBeanDefinition(PlatformTransactionManager.class,
      () -> new ChainedTransactionManager(transactionManagers));

    val beanDefinition = builder.getBeanDefinition();
    beanDefinition.setPrimary(true);
    ((GenericApplicationContext) parent)
      .registerBeanDefinition("transactionManager", beanDefinition);
    components.forEach(component -> {
      val gac = (GenericApplicationContext) component;
      gac.setAllowBeanDefinitionOverriding(true);
      gac.registerBeanDefinition("transactionManager", beanDefinition);
    });
  }

  static class SpringApplicationBroadcastEventPublisher implements EventPublisher {

    private Set<ApplicationEventPublisher> publishers = new HashSet<>();

    public void publishEvent(Event event) {
      publishers.forEach(publisher -> publisher.publishEvent(event));
    }
  }
}
