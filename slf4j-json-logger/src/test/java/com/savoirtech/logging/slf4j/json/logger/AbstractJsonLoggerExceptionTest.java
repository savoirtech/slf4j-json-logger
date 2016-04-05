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
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import static org.mockito.Mockito.mock;

public class AbstractJsonLoggerExceptionTest {
  private AbstractJsonLogger logger;

  private org.slf4j.Logger slf4jLogger;

  private Gson gson;

  private String dateFormatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
  private FastDateFormat formatter;

  private String logMessage;

  @Before
  public void setup() {
    this.slf4jLogger = mock(org.slf4j.Logger.class);

    // Use a special GSON configuration that throws exceptions at the right time for the test.
    this.gson = new GsonBuilder().registerTypeAdapterFactory(new TestTypeAdapterFactory()).create();

    this.formatter = FastDateFormat.getInstance(dateFormatString);

    logger = new AbstractJsonLogger(slf4jLogger, formatter, gson) {
      @Override
      public void log() {
        logMessage = formatMessage("INFO");
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
    assert(logMessage.contains("\"message\":\"java.lang.RuntimeException: x-rt-exc-x"));
  }

  @Test
  public void messageSupplier() {
    logger.message(() -> {throw new RuntimeException("Some error!");}).log();
    assert(logMessage.contains("\"message\":\"java.lang.RuntimeException: Some error!"));
  }

  @Test
  public void map() {
    logger.map("someMap", new HashMap()).log();
    assert(logMessage.contains("\"someMap\":\"java.lang.RuntimeException: x-rt-exc-x"));
  }

  @Test
  public void mapSupplier() {
    logger.map("someMap", () -> {throw new RuntimeException("Some error!");}).log();
    assert(logMessage.contains("\"someMap\":\"java.lang.RuntimeException: Some error!"));
  }

  @Test
  public void list() {
    logger.list("someList", new LinkedList()).log();
    assert(logMessage.contains("\"someList\":\"java.lang.RuntimeException: x-rt-exc-x"));
  }

  @Test
  public void listSupplier() {
    logger.list("someList", () -> {throw new RuntimeException("Some error!");}).log();
    assert(logMessage.contains("\"someList\":\"java.lang.RuntimeException: Some error!"));
  }

  @Test
  public void field() {
    logger.field("key", "value").log();
    assert(logMessage.contains("\"key\":\"java.lang.RuntimeException: x-rt-exc-x"));
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

  @Test
  public void exception() {
    logger.exception("exception", new Exception("x-exc-x")).log();
    assert(logMessage.contains("\"exception\":\"java.lang.RuntimeException: x-rt-exc-x"));
  }

  @Test
  public void mdc() {
    MDC.put("x-mdc-key-x", "x-mdc-value-x");
    logger.log();
    assert (logMessage.contains("\"MDC\":\"java.lang.RuntimeException: x-rt-exc-x"));
  }


  //========================================
  // INTERNALS
  //========================================

  /**
   * JSON type adapter factory that throws exceptions, or for Strings, generates an adapter that
   * throws exceptions at the right time for the test.
   */
  private class TestTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      if (type.getRawType().equals(String.class)) {
        return (TypeAdapter<T>) new TestStringGsonTypeAdapter();
      }

      throw new RuntimeException("x-rt-exc-x");
    }
  }

  /**
   * GSON adapter that throws exceptions at the right time for the test.
   */
  private class TestStringGsonTypeAdapter extends TypeAdapter<String> {

    @Override
    public void write(JsonWriter out, String value) throws IOException {
      //
      // Do not throw exceptions when the AbstractJsonLogger (class under test) is formatting an
      //  exception from this test.  Also do not throw an exception when the logger formats the
      //  "INFO" level.
      //
      if (value.startsWith("java.lang.RuntimeException:") || value.equals("INFO")) {
        out.value(value);
      } else {
        throw new RuntimeException("x-rt-exc-x");
      }
    }

    @Override
    public String read(JsonReader in) throws IOException {
      return null;
    }
  }
}
