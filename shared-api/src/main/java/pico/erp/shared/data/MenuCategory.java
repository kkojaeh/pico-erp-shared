package pico.erp.shared.data;

import lombok.Getter;

public enum MenuCategory implements LocalizedNameable {

  SETTINGS("settings"),
  ITEM("shopping_cart"),
  CUSTOMER("work"),
  PROCESS("fast_forward"),
  WAREHOUSE("layers");

  @Getter
  private final String icon;

  MenuCategory(String icon) {
    this.icon = icon;
  }

}
