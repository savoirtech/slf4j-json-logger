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

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.FieldPosition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Verify operation of the LoggerBuilder class.
 *
 * Created by art on 10/13/16.
 */
public class LoggerBuilderTest {

  private LoggerBuilder loggerBuilder;

  private org.slf4j.Logger mockSlf4jLogger;
  private FastDateFormat mockDateFormatter;

  @Before
  public void setupTest() {
    this.loggerBuilder = new LoggerBuilder();

    this.mockSlf4jLogger = Mockito.mock(org.slf4j.Logger.class);
    this.mockDateFormatter = Mockito.mock(FastDateFormat.class);
  }

  /**
   * Verify operation of the slf4jLogger() builder method.
   */
  @Test
  public void testSlf4jLogger() throws Exception {
    //
    // Setup test data and interactions
    //
    Mockito.when(this.mockSlf4jLogger.isInfoEnabled()).thenReturn(true);

    // WARNING: this is ugly, relying on internals of FastDateFormat.  Unfortunately, the public API
    //  method is declared "final".
    Mockito.when(this.mockDateFormatter.format(Mockito.anyObject(), Mockito.any(StringBuffer.class), Mockito.any(FieldPosition.class)))
        .thenReturn(new StringBuffer("x-formatted-date-x"));

    //
    // Execute
    //
    Logger result =
        this.loggerBuilder
          .slf4jLogger(this.mockSlf4jLogger, this.mockDateFormatter)
            .build();
    result.info().message("x-msg-x").log();

    //
    // Verify
    //
    Mockito.verify(this.mockSlf4jLogger).info(Mockito.matches(".*x-formatted-date-x.*"));
    Mockito.verify(this.mockSlf4jLogger).info(Mockito.matches(".*x-msg-x.*"));
  }

  /**
   * Verify operation of the includeLoggerName() builder method.
   */
  @Test
  public void testIncludeLoggerName() throws Exception {
    //
    // Execute
    //
    Logger includeLoggerNameLogger =
        this.loggerBuilder
          .includeLoggerName(true)
          .build();
    Logger excludeLoggerNameLogger =
        this.loggerBuilder
          .includeLoggerName(false)
          .build();

    //
    // Verify
    //
    assertTrue(includeLoggerNameLogger.isIncludeLoggerName());
    assertFalse(excludeLoggerNameLogger.isIncludeLoggerName());
  }

  /**
   * Verify operation of the includeClassName() builder method.
   */
  @Test
  public void testIncludeClassName() throws Exception {
    //
    // Execute
    //
    Logger includeClassNameLogger =
        this.loggerBuilder
          .includeClassName(true)
          .build();
    Logger excludeClassNameLogger =
        this.loggerBuilder
          .includeClassName(false)
          .build();

    //
    // Verify
    //
    assertTrue(includeClassNameLogger.isIncludeClassName());
    assertFalse(excludeClassNameLogger.isIncludeClassName());
  }

  /**
   * Verify operation of the includeThreadName() builder method.
   */
  @Test
  public void testIncludeThreadName() throws Exception {
    //
    // Execute
    //
    Logger includeThreadNameLogger =
        this.loggerBuilder
          .includeThreadName(true)
          .build();
    Logger excludeThreadNameLogger =
        this.loggerBuilder
          .includeThreadName(false)
          .build();

    //
    // Verify
    //
    assertTrue(includeThreadNameLogger.isIncludeThreadName());
    assertFalse(excludeThreadNameLogger.isIncludeThreadName());
  }
}
