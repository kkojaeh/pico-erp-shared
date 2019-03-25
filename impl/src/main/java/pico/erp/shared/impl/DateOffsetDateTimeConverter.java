package pico.erp.shared.impl;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;

public class DateOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {

  @SneakyThrows
  @Override
  public OffsetDateTime convert(Date source) {
    if (source == null) {
      return null;
    }
    return OffsetDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
  }
}
