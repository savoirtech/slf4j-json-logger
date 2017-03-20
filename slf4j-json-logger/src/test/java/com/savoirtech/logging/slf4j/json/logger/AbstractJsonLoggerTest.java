/*
 * Copyright (c) 2016 Savoir Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.savoirtech.logging.slf4j.json.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AbstractJsonLoggerTest {

  private AbstractJsonLogger logger;

  private org.slf4j.Logger slf4jLogger;

  private Gson gson;

  private String dateFormatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
  private FastDateFormat formatter;

  private String logMessage;

  @Before
  public void setup() {
    this.slf4jLogger = Mockito.mock(org.slf4j.Logger.class);
    this.gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
    this.formatter = FastDateFormat.getInstance(dateFormatString);

    logger = new AbstractJsonLogger(slf4jLogger, formatter, gson, true) {
      @Override
      public void log() {
        logMessage = formatMessage("INFO");
      }

      @Override
      public void log(Marker marker) {
        logMessage = formatMessage(marker.getName(), "INFO");
      }

    };
  }

  @After
  public void cleanupTest() {
    MDC.clear();
  }

  @Test
  public void message() {
    logger.message("message").log();
    assert (logMessage.contains("\"message\":\"message\""));
  }

  @Test
  public void messageSupplier() {
    logger.message(() -> "message").log();
    assert (logMessage.contains("\"message\":\"message\""));
  }

  @Test
  public void map() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    logger.map("someMap", map).log();
    assert (logMessage.contains("\"someMap\":{\"key\":\"value\"}"));
  }

  @Test
  public void mapSupplier() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    logger.map("someMap", () -> map).log();
    assert (logMessage.contains("\"someMap\":{\"key\":\"value\"}"));
  }

  @Test
  public void list() {
    List<String> list = new LinkedList<>();
    list.add("value1");
    list.add("value2");
    logger.list("someList", list).log();
    assert (logMessage.contains("\"someList\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void listSupplier() {
    List<String> list = new LinkedList<>();
    list.add("value1");
    list.add("value2");
    logger.list("someList", () -> list).log();
    assert (logMessage.contains("\"someList\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void field() {
    logger.field("key", "value").log();
    assert (logMessage.contains("\"key\":\"value\""));
  }

  @Test
  public void fieldSupplier() {
    logger.field("key", () -> "value").log();
    assert (logMessage.contains("\"key\":\"value\""));
  }

  @Test
  public void json() {
    JsonElement jsonElement = gson.toJsonTree(new String[]{"value1", "value2"});
    logger.json("json", jsonElement).log();
    assert (logMessage.contains("\"json\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void jsonSupplier() {
    JsonElement jsonElement = gson.toJsonTree(new String[]{"value1", "value2"});
    logger.json("json", () -> jsonElement).log();
    assert (logMessage.contains("\"json\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void exception() {
    logger.exception("myException", new RuntimeException("Something bad")).log();
    assert (logMessage.contains("\"myException\":\"java.lang.RuntimeException: Something bad"));
  }

  @Test
  public void MDC() {
    MDC.put("myMDC", "someValue");
    logger.message("message").log();
    assert (logMessage.contains("\"mdc\":{\"myMDC\":\"someValue\""));
    MDC.clear();
  }

  @Test
  public void containsThreadName() {
    logger.message("Some message").log();
    assert (logMessage.contains("\"thread_name\":\"" + Thread.currentThread().getName() + "\""));
  }

  @Test
  public void containsClassName() {
    logger.message("Some message").log();
    assert (logMessage.contains("\"class\":\"com.savoirtech.logging.slf4j.json.logger.AbstractJsonLoggerTest\""));
  }

  @Test
  public void numberAsString() {
    logger.field("Number as a string", "1.011").log();
    assert (logMessage.contains("\"Number as a string\":\"1.011\""));
  }

  @Test
  public void numberInteger() {
    logger.field("Integer", 42).log();
    assert (logMessage.contains("\"Integer\":42"));
  }

  @Test
  public void numberDouble() {
    logger.field("Double", 1.042).log();
    assert (logMessage.contains("\"Double\":1.042"));
  }

  @Test
  public void numberRepeatingDouble() {
    logger.field("Repeating double", 10.0/3.0).log();
    //avoiding precision/scale issues
    assert (logMessage.contains("\"Repeating double\":3.333"));
  }

  @Test
  public void nullValueIsLogged() {
    logger.field("nullValue", null).log();
    // should not throw a null pointer
    assert (!logMessage.contains("java.lang.NullPointerException"));
    assert (logMessage.contains("\"nullValue\":null"));
  }

  @Test
  public void markerIsUsed() {
    logger.log(MarkerFactory.getMarker("TEST"));
    assert (logMessage.contains("\"marker\":\"TEST\""));
  }
}
