package pico.erp.shared.impl;

import java.time.LocalDateTime;
import org.springframework.core.convert.converter.Converter;

public class StringLocalDateTimeConverter implements Converter<String, LocalDateTime> {

  @Override
  public LocalDateTime convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }
    return LocalDateTime.parse(source);
  }
}
