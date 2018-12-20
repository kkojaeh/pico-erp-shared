package pico.erp.shared.impl;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pico.erp.shared.ExportHelper;
import pico.erp.shared.data.LocalizedNameable;

public class ExportHelperImpl implements ExportHelper {

  private static final String DEFAULT_NUMBER_FORMAT = "###,###.#####";

  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

  @Autowired
  private MessageSource messageSource;

  public static void main(String... args) {

  }

  @Override
  public String formatDate(TemporalAccessor accessor) {
    if (accessor == null) {
      return null;
    }
    return dateFormatter.format(accessor);
  }

  @Override
  public String formatNumber(BigDecimal value) {
    return new DecimalFormat(DEFAULT_NUMBER_FORMAT).format(value);
  }

  @SneakyThrows
  @Override
  public String formatPhoneNumber(String value) {
    PhoneNumber number = phoneUtil.parse(value, getLocale().getCountry());
    return String.format("(+%d) %s", number.getCountryCode(),
      phoneUtil.format(number, PhoneNumberFormat.NATIONAL));
  }

  @Override
  public Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }

  @Override
  public String getMessage(LocalizedNameable nameable) {
    if (nameable == null) {
      return null;
    }
    return messageSource
      .getMessage(nameable.getNameCode(), null, nameable.getDefault(), getLocale());
  }

  public String repeat(String value, int count) {
    return count > 0 ? value + repeat(value, --count) : "";
  }

}
