package com.savoirtech.log.slf4j.json;

import com.savoirtech.log.slf4j.json.logger.Logger;

import org.apache.commons.lang3.time.FastDateFormat;

public class LoggerFactory {
  private static String dateFormatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
  private static FastDateFormat formatter = FastDateFormat.getInstance(dateFormatString);

  public static Logger getLogger(String name) {
    org.slf4j.Logger slf4jLogger = org.slf4j.LoggerFactory.getLogger(name);
    return new Logger(slf4jLogger, formatter);
  }

  public static Logger getLogger(Class<?> clazz) {
    org.slf4j.Logger slf4jLogger = org.slf4j.LoggerFactory.getLogger(clazz);
    return new Logger(slf4jLogger, formatter);
  }

  public static void setDateFormatString(String dateFormatString) {
    LoggerFactory.dateFormatString = dateFormatString;
    LoggerFactory.formatter = FastDateFormat.getInstance(dateFormatString);
  }
}
