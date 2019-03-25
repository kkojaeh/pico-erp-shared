package pico.erp.shared.impl;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;

public class StringOffsetDateTimeConverter implements Converter<String, OffsetDateTime> {

  private final StdDateFormat formatter = new StdDateFormat();

  @SneakyThrows
  @Override
  public OffsetDateTime convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }
    Date date = formatter.parse(source);
    return OffsetDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }
}
