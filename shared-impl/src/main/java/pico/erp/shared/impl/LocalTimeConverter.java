package pico.erp.shared.impl;

import java.time.LocalTime;
import org.springframework.core.convert.converter.Converter;

public class LocalTimeConverter implements Converter<String, LocalTime> {

  @Override
  public LocalTime convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }
    return LocalTime.parse(source);
  }
}
