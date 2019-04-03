package pico.erp.shared;

import java.util.LinkedList;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import lombok.val;
import pico.erp.ComponentDefinition;

public class ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier implements
  Supplier<Class<?>[]> {

  @Override
  public Class<?>[] get() {
    ServiceLoader<ComponentDefinition> loader = ServiceLoader.load(ComponentDefinition.class);
    val components = new LinkedList<Class<?>>();
    loader.forEach(definition -> {
      components.add(definition.getComponentClass());
    });
    return components.toArray(new Class<?>[components.size()]);
  }
}
