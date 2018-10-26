package pico.erp.shared;

import com.querydsl.core.annotations.QueryProjection;
import java.util.UUID;
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

  @QueryProjection
  public ExtendedLabeledValue(UUID value, String label, String subLabel, String stamp) {
    this.value = value != null ? value.toString() : null;
    this.label = label;
    this.subLabel = subLabel;
    this.stamp = stamp;
  }

}
