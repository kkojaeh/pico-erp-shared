package pico.erp.shared.impl;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;

public class DateLocalTimeConverter implements Converter<Date, LocalTime> {

  @Override
  public LocalTime convert(Date source) {
    if (source == null) {
      return null;
    }
    return source.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
  }
}
