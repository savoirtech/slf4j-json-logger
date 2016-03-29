package com.savoirtech.logging.slf4j.json.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.MDC;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AbstractJsonLoggerTests {
  private AbstractJsonLogger logger;

  private org.slf4j.Logger slf4jLogger;

  private Gson gson;

  private String dateFormatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
  private FastDateFormat formatter;

  private String logMessage;

  @Before
  public void setup() {
    this.slf4jLogger = Mockito.mock(org.slf4j.Logger.class);
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
  public void message() {
    logger.message("message").log();
    assert(logMessage.contains("\"message\":\"message\""));
  }

  @Test
  public void messageSupplier() {
    logger.message(() -> "message").log();
    assert(logMessage.contains("\"message\":\"message\""));
  }

  @Test
  public void map() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    logger.map("someMap", map).log();
    assert(logMessage.contains("\"someMap\":{\"key\":\"value\"}"));
  }

  @Test
  public void mapSupplier() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    logger.map("someMap", () -> map).log();
    assert(logMessage.contains("\"someMap\":{\"key\":\"value\"}"));
  }

  @Test
  public void list() {
    List<String> list = new LinkedList<>();
    list.add("value1");
    list.add("value2");
    logger.list("someList", list).log();
    assert(logMessage.contains("\"someList\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void listSupplier() {
    List<String> list = new LinkedList<>();
    list.add("value1");
    list.add("value2");
    logger.list("someList", () -> list).log();
    assert(logMessage.contains("\"someList\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void field() {
    logger.field("key", "value").log();
    assert(logMessage.contains("\"key\":\"value\""));
  }

  @Test
  public void fieldSupplier() {
    logger.field("key", () -> "value").log();
    assert(logMessage.contains("\"key\":\"value\""));
  }

  @Test
  public void json() {
    JsonElement jsonElement = gson.toJsonTree(new String[]{"value1", "value2"});
    logger.json("json", jsonElement).log();
    assert(logMessage.contains("\"json\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void jsonSupplier() {
    JsonElement jsonElement = gson.toJsonTree(new String[]{"value1", "value2"});
    logger.json("json", () -> jsonElement).log();
    assert(logMessage.contains("\"json\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void exception() {
    logger.exception("myException", new RuntimeException("Something bad")).log();
    assert(logMessage.contains("\"myException\":[\"java.lang.RuntimeException: Something bad\""));
  }

  @Test
  public void MDC() {
    MDC.put("myMDC", "someValue");
    logger.message("message").log();
    assert(logMessage.contains("\"MDC\":{\"myMDC\":\"someValue\"}"));
    MDC.clear();
  }
}
