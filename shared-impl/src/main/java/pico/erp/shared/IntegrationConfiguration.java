package pico.erp.shared;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import pico.erp.shared.impl.ApplicationImpl;
import pico.erp.shared.impl.ApplicationIntegratorImpl;

@Configuration
public class IntegrationConfiguration implements InitializingBean {

  @Autowired
  private ConfigurableApplicationContext context;

  @Override
  public void afterPropertiesSet() throws Exception {

    List<String> properties = new LinkedList<>();

    MutablePropertySources sources = context.getEnvironment().getPropertySources();
    for (PropertySource source : sources) {
      if (!(source instanceof MapPropertySource)) {
        continue;
      }
      if ("systemProperties".equals(source.getName())) {
        continue;
      }
      MapPropertySource mapSource = (MapPropertySource) source;
      for (String name : mapSource.getPropertyNames()) {
        Object property = mapSource.getProperty(name);
        if(property == null || "".equals(property)){
          continue;
        }
        properties.add(String.format("--%s=%s", name, property));
      }
    }

    ApplicationIntegrator integrator = new ApplicationIntegratorImpl();
    integrator.add(new ApplicationImpl(context));
    ServiceLoader<ApplicationStarter> loader = ServiceLoader.load(ApplicationStarter.class);
    List<ApplicationStarter> starters = StreamSupport.stream(loader.spliterator(), false)
      .filter(starter -> !starter.isWeb())
      .collect(Collectors.toList());

    Collections.sort(starters);
    starters.forEach(
      starter -> integrator.add(starter.start(properties.toArray(new String[properties.size()]))));
    integrator.integrate().integrateTransaction();
    if (context.getEnvironment().acceptsProfiles("test")) {
      integrator.integrateEventPublisher();
    }
    try {
      integrator.complete();
    } catch (Throwable t) {
      t.printStackTrace();
      context.registerShutdownHook();
    }
  }

}
