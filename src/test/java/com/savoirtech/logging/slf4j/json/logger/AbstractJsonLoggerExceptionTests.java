package com.savoirtech.logging.slf4j.json.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class AbstractJsonLoggerExceptionTests {
  private AbstractJsonLogger logger;

  private org.slf4j.Logger slf4jLogger;

  private Gson gson;

  private String dateFormatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
  private FastDateFormat formatter;

  private String logMessage;

  @Before
  public void setup() {
    this.slf4jLogger = mock(org.slf4j.Logger.class);
    this.gson = new GsonBuilder().disableHtmlEscaping().create();
    this.formatter = FastDateFormat.getInstance(dateFormatString);

    logger = new AbstractJsonLogger(slf4jLogger, formatter, gson) {
      @Override
      public void log() {
        logMessage = formatMessage("INFO");
      }
    };
  }

  @Test
  public void messageSupplier() {
    logger.message(() -> {throw new RuntimeException("Some error!");}).log();
    assert(logMessage.contains("\"message\":\"java.lang.RuntimeException: Some error!"));
  }

  @Test
  public void mapSupplier() {
    logger.map("someMap", () -> {throw new RuntimeException("Some error!");}).log();
    assert(logMessage.contains("\"someMap\":\"java.lang.RuntimeException: Some error!"));
  }

  @Test
  public void listSupplier() {
    logger.list("someList", () -> {throw new RuntimeException("Some error!");}).log();
    assert(logMessage.contains("\"someList\":\"java.lang.RuntimeException: Some error!"));
  }

  @Test
  public void fieldSupplier() {
    logger.field("key", () -> {throw new RuntimeException("Some error!");}).log();
    assert(logMessage.contains("\"key\":\"java.lang.RuntimeException: Some error!"));
  }

  @Test
  public void jsonSupplier() {
    logger.json("json", () -> {throw new RuntimeException("Some error!");}).log();
    assert(logMessage.contains("\"json\":\"java.lang.RuntimeException: Some error!"));
  }
}
