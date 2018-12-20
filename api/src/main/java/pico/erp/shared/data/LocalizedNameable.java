package pico.erp.shared.data;

public interface LocalizedNameable {

  default String getDefault() {
    return name();
  }

  default String getNameCode() {
    return getClass().getName() + "." + name();
  }

  String name();

}
