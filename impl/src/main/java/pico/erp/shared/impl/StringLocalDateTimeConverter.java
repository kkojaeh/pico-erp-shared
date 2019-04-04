package pico.erp.shared.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;

public class StringLocalDateTimeConverter implements Converter<String, LocalDateTime> {

  @Override
  public LocalDateTime convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }
    if (source.length() > 10 && source.charAt(10) == 'T') {
      if (source.endsWith("Z")) {
        return LocalDateTime.ofInstant(Instant.parse(source), ZoneId.systemDefault());
      } else {
        return LocalDateTime.parse(source, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      }
    }
    return LocalDateTime.parse(source);
  }

}
