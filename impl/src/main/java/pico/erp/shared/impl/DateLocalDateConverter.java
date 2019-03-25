package pico.erp.shared.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;

public class DateLocalDateConverter implements Converter<Date, LocalDate> {

  @Override
  public LocalDate convert(Date source) {
    if (source == null) {
      return null;
    }
    return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }
}
