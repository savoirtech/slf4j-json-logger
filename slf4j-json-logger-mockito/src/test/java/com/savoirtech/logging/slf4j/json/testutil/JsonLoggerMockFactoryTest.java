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

package com.savoirtech.logging.slf4j.json.testutil;

import com.google.gson.JsonPrimitive;

import com.savoirtech.logging.slf4j.json.logger.JsonLogger;

import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Verify operation of the JsonLoggerMockFactory.
 *
 * Created by art on 3/31/16.
 */
public class JsonLoggerMockFactoryTest {

  private JsonLoggerMockFactory factory;

  @org.junit.Before
  public void setupTest() throws Exception {
    this.factory = new JsonLoggerMockFactory();
  }

  @org.junit.Test
  public void testCreateJsonLoggerMock() throws Exception {
    // Get the first logger
    JsonLogger firstLogger = this.factory.createJsonLoggerMock();

    // Validate it
    this.validateMockJsonLogger(firstLogger);

    // Get another logger
    JsonLogger secondLogger = this.factory.createJsonLoggerMock();

    // Validate it
    this.validateMockJsonLogger(firstLogger);

    // Make sure the two are unique
    assertNotSame(firstLogger, secondLogger);
  }

  /**
   * Simulate actual use and validation of a logger.
   *
   * @throws Exception
   */
  @org.junit.Test
  public void testMockUsingExpectedSyntax() throws Exception {
    // Setup
    JsonLogger logger = this.factory.createJsonLoggerMock();

    // Run (inline here, but would be within a class-under-test in real usage scenarios)
    logger.message("something went wrong with the application").field("user-id", "joe")
        .list("tokens", Arrays.asList("token1", "token2")).log();

    // Validate
    Mockito.verify(logger).message("something went wrong with the application");
    Mockito.verify(logger).field("user-id", "joe");
    Mockito.verify(logger).list("tokens", Arrays.asList("token1", "token2"));
    Mockito.verify(logger).log();
  }

  /**
   * Validate the mock json logger given is properly configured to return itself on all
   * builder-style method calls.
   *
   * @param jsonLogger
   * @throws Exception
   */
  private void validateMockJsonLogger(JsonLogger jsonLogger) throws Exception {
    // exception
    assertSame(jsonLogger, jsonLogger.exception("x-exc-x", new Exception()));

    // field
    assertSame(jsonLogger, jsonLogger.field("x-field-name-x", "x-field-value-x"));
    assertSame(jsonLogger, jsonLogger.field("x-field-name-x", () -> "x-field-value-x"));

    // json
    assertSame(jsonLogger, jsonLogger.json("x-json-name-x", new JsonPrimitive("x-primitive-x")));
    assertSame(jsonLogger, jsonLogger.json("x-json-name-x", () -> new JsonPrimitive("x-primitive-x")));

    // list
    assertSame(jsonLogger, jsonLogger.list("x-list-name-x", Arrays.asList("x-val1-x", "x-val2-x")));
    assertSame(jsonLogger, jsonLogger.list("x-list-name-x", () -> Arrays.asList("x-val1-x", "x-val2-x")));

    // map
    Map<String, String> localMap = new HashMap<>();
    localMap.put("x-sub-field1-x", "x-sub-value1-x");
    localMap.put("x-sub-field2-x", "x-sub-value2-x");

    assertSame(jsonLogger, jsonLogger.map("x-map-name-x", localMap));
    assertSame(jsonLogger, jsonLogger.map("x-map-name-x", () -> localMap));

    // message
    assertSame(jsonLogger, jsonLogger.message("x-message-x"));
    assertSame(jsonLogger, jsonLogger.message(() -> "x-message-x"));
  }
}