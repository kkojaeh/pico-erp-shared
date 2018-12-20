package pico.erp.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan
@Configuration
@EntityScan
@EnableJpaRepositories
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
@Import(value = {
  PropertyPlaceholderAutoConfiguration.class,
  MessageSourceAutoConfiguration.class,
  JndiConnectionFactoryAutoConfiguration.class,
  ActiveMQAutoConfiguration.class,
  JmsAutoConfiguration.class,
  CacheAutoConfiguration.class,
  HibernateJpaAutoConfiguration.class,
  DataSourceAutoConfiguration.class,
  FlywayAutoConfiguration.class,
  ValidationAutoConfiguration.class,
  SharedConfiguration.class,
  TransactionAutoConfiguration.class,
  PersistenceExceptionTranslationAutoConfiguration.class,
})

public @interface SpringBootConfigs {

}
