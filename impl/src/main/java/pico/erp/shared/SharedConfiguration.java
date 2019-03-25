package pico.erp.shared;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.sql.DatabaseMetaData;
import java.util.Currency;
import javax.jms.ConnectionFactory;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.DestinationInterceptor;
import org.apache.activemq.broker.region.virtual.VirtualTopic;
import org.apache.activemq.transport.vm.VMTransportFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ErrorHandler;
import pico.erp.shared.event.EventPublisher;
import pico.erp.shared.impl.ExportHelperImpl;
import pico.erp.shared.impl.JmsEventPublisher;
import pico.erp.shared.impl.LocalDateTimeOffsetDateTimeConverter;
import pico.erp.shared.impl.QueryDslJpaSupportImpl;
import pico.erp.shared.impl.SpringApplicationEventPublisher;
import pico.erp.shared.impl.StringLocalDateConverter;
import pico.erp.shared.impl.StringLocalTimeConverter;
import pico.erp.shared.impl.StringOffsetDateTimeConverter;
import pico.erp.shared.jpa.AuditorAwareImpl;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@EntityScan(
  basePackageClasses = Jsr310JpaConverters.class
)
@Configuration
public class SharedConfiguration {

  @Lazy
  @Autowired
  private ErrorHandler errorHandler;

  @Bean
  public AuditorAware auditorAware() {
    return new AuditorAwareImpl();
  }

  @Bean
  public Currency currency() {
    return Currency.getInstance("KRW");
  }

  @Bean
  public DataSourceLogger dataSourceLogger() {
    return new DataSourceLogger();
  }

  @Bean
  public InitializingBean embeddedBrokerServiceCustomizer() {
    return () -> {
      BrokerService embedded = VMTransportFactory.BROKERS.get("embedded");
      if (embedded != null) {
        VirtualTopic virtualTopic = new VirtualTopic();
        virtualTopic.setPrefix("listener.*.");
        virtualTopic.setPostfix("event.");
        embedded.setDestinationInterceptors(new DestinationInterceptor[]{virtualTopic});
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(EventPublisher.class)
  @Profile("test")
  public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    return new SpringApplicationEventPublisher(applicationEventPublisher);
  }

  @Bean
  public ExportHelper exportHelper() {
    return new ExportHelperImpl();
  }

  @Bean
  @Profile({"!test"})
  @ConditionalOnMissingBean(EventPublisher.class)
  public EventPublisher jmsEventPublisher() {
    return new JmsEventPublisher();
  }

  @Bean
  public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
    ConnectionFactory connectionFactory,
    DefaultJmsListenerContainerFactoryConfigurer configurer) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setErrorHandler(errorHandler);
    configurer.configure(factory, connectionFactory);
    return factory;
  }

  @Bean
  public MessageConverter jmsMessageConverter() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.registerModule(new JavaTimeModule());
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setObjectMapper(mapper);
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter;
  }

  @Bean
  public Converter localDateTimeOffsetDateTimeConverter() {
    return new LocalDateTimeOffsetDateTimeConverter();
  }

  @Bean
  @ConfigurationPropertiesBinding
  public Converter stringLocalDateConverter() {
    return new StringLocalDateConverter();
  }

  @Bean
  @ConfigurationPropertiesBinding
  public Converter stringLocalTimeConverter() {
    return new StringLocalTimeConverter();
  }

  @Bean
  @ConfigurationPropertiesBinding
  public Converter stringOffsetDateTimeConverter() {
    return new StringOffsetDateTimeConverter();
  }

  @Bean
  public ErrorHandler loggingErrorHandler() {
    return new LoggingErrorHandler();
  }


  @Bean
  public QueryDslJpaSupport queryDslJpaSupport() {
    return new QueryDslJpaSupportImpl('\\');
  }

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler threadPoolTaskScheduler
      = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(5);
    threadPoolTaskScheduler.setThreadNamePrefix("default");
    return threadPoolTaskScheduler;
  }

  @Slf4j
  public static class DataSourceLogger implements InitializingBean {

    @Autowired(required = false)
    private DataSource dataSource;

    @Override
    public void afterPropertiesSet() throws Exception {
      if (dataSource != null) {
        DatabaseMetaData databaseMetaData = dataSource.getConnection().getMetaData();
        log.info("Database: {}- {} - {} - {}", databaseMetaData.getDatabaseProductName(),
          databaseMetaData.getDatabaseProductVersion(), databaseMetaData.getDriverName(),
          databaseMetaData.getURL());
      }
    }
  }

  @Slf4j
  private static class LoggingErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
      log.error(t.getMessage(), t);
    }
  }

}
