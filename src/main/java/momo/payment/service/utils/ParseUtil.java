package momo.payment.service.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import momo.payment.service.exception.ParseIntException;
import momo.payment.service.exception.ParseLocalDateException;

public class ParseUtil {
  public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public static int parseInt(String string) {
    try {
      return Integer.parseInt(string);
    } catch (Exception e) {
      throw new ParseIntException();
    }
  }

  public static LocalDate parseLocalDate(String string) {
    try {
      return LocalDate.parse(string, DATE_TIME_FORMATTER);
    } catch (Exception e) {
      throw new ParseLocalDateException();
    }
  }
}
