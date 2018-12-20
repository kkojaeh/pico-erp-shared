package pico.erp.shared.impl;

import java.time.LocalDate;
import org.springframework.core.convert.converter.Converter;

public class LocalDateConverter implements Converter<String, LocalDate> {

  @Override
  public LocalDate convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }
    return LocalDate.parse(source);
  }
}
