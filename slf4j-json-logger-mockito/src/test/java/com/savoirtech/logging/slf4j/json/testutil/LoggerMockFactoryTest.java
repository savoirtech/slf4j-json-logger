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

import com.savoirtech.logging.slf4j.json.logger.JsonLogger;
import com.savoirtech.logging.slf4j.json.logger.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Verify operation of the LoggerMockFactory.
 *
 * Created by art on 3/31/16.
 */
public class LoggerMockFactoryTest {

  private LoggerMockFactory factory;

  private JsonLoggerMockFactory mockJsonLoggerMockFactory;
  private JsonLogger mockJsonLogger1;
  private JsonLogger mockJsonLogger2;

  /**
   * Setup common test interactions and data.
   *
   * @throws Exception
   */
  @Before
  public void setupTest() throws Exception {
    this.factory = new LoggerMockFactory();

    this.mockJsonLoggerMockFactory = Mockito.mock(JsonLoggerMockFactory.class);
    this.mockJsonLogger1 = Mockito.mock(JsonLogger.class);
    this.mockJsonLogger2 = Mockito.mock(JsonLogger.class);

    Mockito.when(this.mockJsonLoggerMockFactory.createJsonLoggerMock())
        .thenReturn(this.mockJsonLogger1, this.mockJsonLogger2);
  }

  /**
   * Verify the getter and setter for the jsonLoggerMockFactory.
   *
   * @throws Exception
   */
  @Test
  public void testGetSetJsonLoggerMockFactory() throws Exception {
    // Verify initial value is not null (and verify no test bleed)
    assertNotNull(this.factory.getJsonLoggerMockFactory());
    assertNotEquals(this.mockJsonLoggerMockFactory, this.factory.getJsonLoggerMockFactory());

    // Set a new value
    this.factory.setJsonLoggerMockFactory(this.mockJsonLoggerMockFactory);

    // Verify the new value
    assertSame(this.mockJsonLoggerMockFactory, this.factory.getJsonLoggerMockFactory());
  }

  /**
   * Verify the creation of mocks using the factory.
   *
   * @throws Exception
   */
  @Test
  public void testCreateLoggerMock() throws Exception {
    // Use the mock jsonLoggerMockFactory.
    this.factory.setJsonLoggerMockFactory(this.mockJsonLoggerMockFactory);

    // Create one mock logger
    Logger mockLogger1 = this.factory.createLoggerMock();

    // Verify this mock logger
    this.validateLoggerMock(mockLogger1, this.mockJsonLogger1);

    // Create another mock logger
    Logger mockLogger2 = this.factory.createLoggerMock();

    // Make sure it is unique
    assertNotSame(mockLogger1, mockLogger2);

    // Verify the second logger
    this.validateLoggerMock(mockLogger2, this.mockJsonLogger2);
  }

  /**
   * Verify the expected use of the mock logger.  This also serves as an example.
   *
   * @throws Exception
   */
  @Test
  public void testExpectedUse() throws Exception {
    // Setup the test.
    Logger mockLogger = this.factory.createLoggerMock();

    // Run (inline here, but would be within a class-under-test in real usage scenarios)
    mockLogger.error().message("found an error").field("correlation-id", "x-correlation-id-x").log();

    // Verify
    Mockito.verify(mockLogger).error();
    // Get the mock json logger from the mock logger
    JsonLogger mockJsonLogger = mockLogger.error();
    Mockito.verify(mockJsonLogger).message("found an error");
    Mockito.verify(mockJsonLogger).field("correlation-id", "x-correlation-id-x");
    Mockito.verify(mockJsonLogger).log();
  }

  /**
   * Verify the given mock logger is properly configured to return the given jsonLogger when used.
   *
   * @param logger
   * @param jsonLogger
   * @throws Exception
   */
  private void validateLoggerMock(Logger logger, JsonLogger jsonLogger) throws Exception {
    assertSame(jsonLogger, logger.trace());
    assertSame(jsonLogger, logger.debug());
    assertSame(jsonLogger, logger.info());
    assertSame(jsonLogger, logger.warn());
    assertSame(jsonLogger, logger.error());
  }
}
