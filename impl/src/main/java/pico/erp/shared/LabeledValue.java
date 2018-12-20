package pico.erp.shared;

import com.querydsl.core.annotations.QueryProjection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pico.erp.shared.data.LabeledValuable;

@SuppressWarnings("Annotator")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LabeledValue implements LabeledValuable {

  public static Pattern codePattern = Pattern.compile("\\([^\\(\\)]+\\)$");

  String value;

  String label;

  @QueryProjection
  public LabeledValue(UUID value, String label) {
    this.value = value != null ? value.toString() : null;
    this.label = label;
  }

  public LabeledValue(String value, String label) {
    this.value = value;
    this.label = label;
  }

  public LabeledValue(String labeledValue) {
    this.value = asValue(labeledValue);
    this.label = asLabel(labeledValue);
  }

  public static String asLabel(String labeledValue) {
    if (labeledValue == null) {
      return null;
    }
    return labeledValue.replaceFirst(codePattern.pattern(), "");
  }

  public static String asValue(String labeledValue) {
    if (labeledValue == null) {
      return null;
    }
    Matcher matcher = codePattern.matcher(labeledValue);
    if (matcher.find()) {
      String codeWithBrace = matcher.group();
      return codeWithBrace.substring(1, codeWithBrace.length() - 1);
    }
    return null;
  }


  @Override
  public String toString() {
    return label + "(" + value + ")";
  }

}
