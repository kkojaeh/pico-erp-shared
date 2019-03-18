package pico.erp.shared.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UnitKind implements LocalizedNameable {

  EA(UnitTypeKind.QUANTITY, 0),
  SHEET(UnitTypeKind.QUANTITY, 0),
  MM(UnitTypeKind.LENGTH, 0),
  CM(UnitTypeKind.LENGTH, 0),
  M(UnitTypeKind.LENGTH, 0),
  KM(UnitTypeKind.LENGTH, 0),

  M2(UnitTypeKind.AREA, 0),

  MG(UnitTypeKind.WEIGHT, 0),
  G(UnitTypeKind.WEIGHT, 0),
  KG(UnitTypeKind.WEIGHT, 0),
  T(UnitTypeKind.WEIGHT, 0),

  CC(UnitTypeKind.VOLUME, 0),
  ML(UnitTypeKind.VOLUME, 0),
  DL(UnitTypeKind.VOLUME, 0),
  L(UnitTypeKind.VOLUME, 0),
  CM3(UnitTypeKind.VOLUME, 0),
  M3(UnitTypeKind.VOLUME, 0);

  @Getter
  private final UnitTypeKind type;

  @Getter
  private final int precision;

}
