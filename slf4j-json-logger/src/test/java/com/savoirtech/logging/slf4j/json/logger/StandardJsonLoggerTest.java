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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StandardJsonLoggerTest {

  private StandardJsonLogger logger;

  private org.slf4j.Logger slf4jLogger;
  private Consumer<String> messageConsumer;
  private BiConsumer<Marker, String> markerMessageConsumer;
  private Marker marker;

  private Gson gson;

  private String dateFormatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
  private FastDateFormat formatter;

  @Before
  public void setup() {
    this.slf4jLogger = Mockito.mock(org.slf4j.Logger.class);
    this.messageConsumer = Mockito.mock(Consumer.class);
    this.markerMessageConsumer = Mockito.mock(BiConsumer.class);
    this.marker = Mockito.mock(Marker.class);

    this.gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
    this.formatter = FastDateFormat.getInstance(dateFormatString);

    logger = new StandardJsonLogger(slf4jLogger, formatter, gson, "WARN", this.messageConsumer, this.markerMessageConsumer);
  }

  @After
  public void cleanupTest() {
    MDC.clear();
  }

  @Test
  public void testGetSetIncludeClassName() {
    assertTrue(logger.isIncludeClassName());
    logger.setIncludeClassName(false);
    assertFalse(logger.isIncludeClassName());
  }

  @Test
  public void testGetSetIncludeThreadName() {
    assertTrue(logger.isIncludeThreadName());
    logger.setIncludeThreadName(false);
    assertFalse(logger.isIncludeThreadName());
  }

  @Test
  public void testGetSetIncludeLoggerName() {
    assertTrue(logger.isIncludeLoggerName());
    logger.setIncludeLoggerName(false);
    assertFalse(logger.isIncludeLoggerName());
  }

  @Test
  public void message() {
    logger.message("message").log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"msg\":\"message\""));
  }

  @Test
  public void messageSupplier() {
    logger.message(() -> "message").log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"msg\":\"message\""));
  }

  @Test
  public void map() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    logger.map("someMap", map).log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"someMap\":{\"key\":\"value\"}"));
  }

  @Test
  public void mapSupplier() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    logger.map("someMap", () -> map).log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"someMap\":{\"key\":\"value\"}"));
  }

  @Test
  public void list() {
    List<String> list = new LinkedList<>();
    list.add("value1");
    list.add("value2");
    logger.list("someList", list).log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"someList\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void listSupplier() {
    List<String> list = new LinkedList<>();
    list.add("value1");
    list.add("value2");
    logger.list("someList", () -> list).log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"someList\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void field() {
    logger.field("key", "value").log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"key\":\"value\""));
  }

  @Test
  public void fieldSupplier() {
    logger.field("key", () -> "value").log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"key\":\"value\""));
  }

  @Test
  public void json() {
    JsonElement jsonElement = gson.toJsonTree(new String[]{"value1", "value2"});
    logger.json("json", jsonElement).log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"json\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void jsonSupplier() {
    JsonElement jsonElement = gson.toJsonTree(new String[]{"value1", "value2"});
    logger.json("json", () -> jsonElement).log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"json\":[\"value1\",\"value2\"]"));
  }

  @Test
  public void exception() {
    logger.exception("myException", new RuntimeException("Something bad")).log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"myException\":\"java.lang.RuntimeException: Something bad"));
  }

  @Test
  public void MDC() {
    MDC.put("myMDC", "someValue");
    logger.message("message").log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"mdc\":{\"myMDC\":\"someValue\""));
    MDC.clear();
  }

  @Test
  public void containsThreadName() {
    logger.message("Some message").log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"thread_name\":\"" + Thread.currentThread().getName() + "\""));
  }

  @Test
  public void containsClassName() {
    logger.message("Some message").log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"class\":\"" + this.getClass().getName() + "\""));
  }

  @Test
  public void numberAsString() {
    logger.field("Number as a string", "1.011").log();
    Mockito.verify(this.messageConsumer).accept(
    Mockito.contains("\"Number as a string\":\"1.011\""));
  }

  @Test
  public void numberInteger() {
    logger.field("Integer", 42).log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"Integer\":42"));
  }

  @Test
  public void numberDouble() {
    logger.field("Double", 1.042).log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"Double\":1.042"));
  }

  @Test
  public void numberRepeatingDouble() {
    logger.field("Repeating double", 3.333).log();
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"Repeating double\":3.333"));
  }

  @Test
  public void nullValueIsLogged() {
    logger.field("nullValue", null).log();
    // should not throw a null pointer
    Mockito.verify(this.messageConsumer, Mockito.times(0)).accept(Mockito.contains("java.lang.NullPointerException"));
    Mockito.verify(this.messageConsumer).accept(Mockito.contains("\"nullValue\":null"));
  }

  /**
   * Verify operation of the marker() method.
   */
  @Test
  public void testMarker() throws Exception {
    logger.marker(this.marker).log();

    Mockito.verify(this.markerMessageConsumer).accept(Mockito.eq(this.marker), Mockito.contains("\"marker\":"));
  }
}
