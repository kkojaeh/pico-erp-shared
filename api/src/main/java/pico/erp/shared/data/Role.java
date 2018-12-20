package pico.erp.shared.data;

public interface Role extends LocalizedNameable, LocalizedDescriptable {

  @Override
  default String getDescriptionCode() {
    return this.getClass().getName() + "." + getId() + ".description";
  }

  String getId();

}
