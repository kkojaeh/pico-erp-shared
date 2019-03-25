package pico.erp.shared.impl;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;

public class LocalDateTimeOffsetDateTimeConverter implements
  Converter<LocalDateTime, OffsetDateTime> {

  @SneakyThrows
  @Override
  public OffsetDateTime convert(LocalDateTime source) {
    if (source == null) {
      return null;
    }
    return source.atOffset(ZoneOffset.of(ZoneId.systemDefault().getId()));
  }
}
