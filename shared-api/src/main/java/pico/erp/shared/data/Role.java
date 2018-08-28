package pico.erp.shared.data;

import java.util.Set;

public interface Role extends LocalizedNameable, LocalizedDescriptable {

  @Override
  default String getDescriptionCode() {
    return this.getClass().getName() + "." + getId() + ".description";
  }

  String getId();

  Set<Menu> getMenus();

}
