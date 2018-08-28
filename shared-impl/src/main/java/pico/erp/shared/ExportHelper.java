package pico.erp.shared;

import java.math.BigDecimal;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import pico.erp.shared.data.LocalizedNameable;

public interface ExportHelper {

  String formatDate(TemporalAccessor accessor);

  String formatNumber(BigDecimal value);

  String formatPhoneNumber(String value);

  Locale getLocale();

  String getMessage(LocalizedNameable nameable);

  String repeat(String value, int count);

}
