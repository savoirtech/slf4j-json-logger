package com.savoirtech.log.slf4j.json.logger;

import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.time.FastDateFormat;

public class InfoLogger extends AbstractJsonLogger {

  public static final String LOG_LEVEL = "INFO";

  public InfoLogger(org.slf4j.Logger slf4jLogger, FastDateFormat formatter, GsonBuilder gsonBuilder) {
    super(slf4jLogger, formatter, gsonBuilder);
  }

  @Override
  public void log() {
    try {
      slf4jLogger.info(formatMessage(LOG_LEVEL));
    }
    catch (Exception e) {
      slf4jLogger.info("{\"" + e + "\"}");
    }
  }

  public String toString() {
    return formatMessage(LOG_LEVEL);
  }
}
