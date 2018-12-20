package pico.erp.shared.impl;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.util.Date;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;

public class DateConverter implements Converter<String, Date> {

  private final StdDateFormat formatter = new StdDateFormat();

  @SneakyThrows
  @Override
  public Date convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }
    return formatter.parse(source);
  }

}
