package com.demo.rss.parser.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

  public static LocalDate parseDate(String dateString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM uuuu HH:mm:ss X");
    return LocalDate.parse(dateString, formatter);
  }
}
