package pico.erp.shared.data;

public interface Menu extends LocalizedNameable {

  MenuCategory getCategory();

  String getIcon();

  String getId();

  String getUrl();

}
