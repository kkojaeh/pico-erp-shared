package pico.erp.shared.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UnitKind implements LocalizedNameable {

  EA(UnitTypeKind.QUANTITY),
  SHEET(UnitTypeKind.QUANTITY),
  MM(UnitTypeKind.LENGTH),
  CM(UnitTypeKind.LENGTH),
  M(UnitTypeKind.LENGTH),
  KM(UnitTypeKind.LENGTH),

  M2(UnitTypeKind.AREA),

  MG(UnitTypeKind.WEIGHT),
  G(UnitTypeKind.WEIGHT),
  KG(UnitTypeKind.WEIGHT),
  T(UnitTypeKind.WEIGHT),

  CC(UnitTypeKind.VOLUME),
  ML(UnitTypeKind.VOLUME),
  DL(UnitTypeKind.VOLUME),
  L(UnitTypeKind.VOLUME),
  CM3(UnitTypeKind.VOLUME),
  M3(UnitTypeKind.VOLUME);

  private final UnitTypeKind type;

  public UnitTypeKind type() {
    return type;
  }

}
