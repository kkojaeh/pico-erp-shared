package pico.erp.shared;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pico.erp.shared.data.LabeledValuable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExtendedLabeledValue implements LabeledValuable {

  String value;

  String label;

  String subLabel;

  String stamp;

}
