package com.savoirtech.log.slf4j.json.logger;

import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Wrapper for slf4j Logger that enables a builder pattern and JSON layout
 */
public class Logger {
  private org.slf4j.Logger slf4jLogger;

  private GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
  private FastDateFormat formatter;

  private NoopLogger noopLogger = new NoopLogger();

  public Logger(org.slf4j.Logger slf4jLogger, FastDateFormat formatter) {
    this.slf4jLogger = slf4jLogger;
    this.formatter = formatter;
  }

  public JsonLogger trace() {
    if (slf4jLogger.isTraceEnabled()) {
      return new TraceLogger(slf4jLogger, formatter, gsonBuilder);
    }

    return noopLogger;
  }

  public JsonLogger debug() {
    if (slf4jLogger.isDebugEnabled()) {
      return new DebugLogger(slf4jLogger, formatter, gsonBuilder);
    }

    return noopLogger;
  }

  public JsonLogger info() {
    if (slf4jLogger.isInfoEnabled()) {
      return new InfoLogger(slf4jLogger, formatter, gsonBuilder);
    }

    return noopLogger;
  }

  public JsonLogger warn() {
    if (slf4jLogger.isWarnEnabled()) {
      return new WarnLogger(slf4jLogger, formatter, gsonBuilder);
    }

    return noopLogger;
  }

  public JsonLogger error() {
    if (slf4jLogger.isErrorEnabled()) {
      return new ErrorLogger(slf4jLogger, formatter, gsonBuilder);
    }

    return noopLogger;
  }
}
