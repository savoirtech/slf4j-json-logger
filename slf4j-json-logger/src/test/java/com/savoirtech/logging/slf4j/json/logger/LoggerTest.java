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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Validate operation of the Logger.
 *
 * Created by art on 3/28/16.
 */
public class LoggerTest {

  private Logger logger;

  private org.slf4j.Logger slf4jLogger;
  private FastDateFormat formatter;

  @Before
  public void setupTest() throws Exception {
    this.slf4jLogger = Mockito.mock(org.slf4j.Logger.class);
    this.formatter = Mockito.mock(FastDateFormat.class);

    this.logger = new Logger(slf4jLogger, formatter);
  }

  @Test
  public void testGetSetIncludeClassName() {
    assertTrue(this.logger.isIncludeClassName());
    this.logger.setIncludeClassName(false);
    assertFalse(this.logger.isIncludeClassName());
  }

  @Test
  public void testGetSetIncludeLoggerName() {
    assertTrue(this.logger.isIncludeLoggerName());
    this.logger.setIncludeLoggerName(false);
    assertFalse(this.logger.isIncludeLoggerName());
  }

  @Test
  public void testGetSetIncludeThreadName() {
    assertTrue(this.logger.isIncludeThreadName());
    this.logger.setIncludeThreadName(false);
    assertFalse(this.logger.isIncludeThreadName());
  }

  @Test
  public void testTraceWhenEnabled() throws Exception {
    Mockito.when(this.slf4jLogger.isTraceEnabled()).thenReturn(true);

    this.logger.trace().message("x-trace-msg-x").log();

    Mockito.verify(this.slf4jLogger).trace(Mockito.contains("\"msg\":\"x-trace-msg-x\""));
  }

  @Test
  public void testTraceWhenDisabled() throws Exception {
    Mockito.when(this.slf4jLogger.isTraceEnabled()).thenReturn(false);

    JsonLogger result = this.logger.trace();

    assertTrue(result instanceof NoopLogger);
  }

  @Test
  public void testDebugWhenEnabled() throws Exception {
    Mockito.when(this.slf4jLogger.isDebugEnabled()).thenReturn(true);

    this.logger.debug().message("x-debug-msg-x").log();

    Mockito.verify(this.slf4jLogger).debug(Mockito.contains("\"msg\":\"x-debug-msg-x\""));
  }

  @Test
  public void testDebugWhenDisabled() throws Exception {
    Mockito.when(this.slf4jLogger.isDebugEnabled()).thenReturn(false);

    JsonLogger result = this.logger.debug();

    assertTrue(result instanceof NoopLogger);
  }

  @Test
  public void testInfoWhenEnabled() throws Exception {
    Mockito.when(this.slf4jLogger.isInfoEnabled()).thenReturn(true);

    this.logger.info().message("x-info-msg-x").log();

    Mockito.verify(this.slf4jLogger).info(Mockito.contains("\"msg\":\"x-info-msg-x\""));
  }

  @Test
  public void testInfoWhenDisabled() throws Exception {
    Mockito.when(this.slf4jLogger.isInfoEnabled()).thenReturn(false);

    JsonLogger result = this.logger.info();

    assertTrue(result instanceof NoopLogger);
  }

  @Test
  public void testWarnWhenEnabled() throws Exception {
    Mockito.when(this.slf4jLogger.isWarnEnabled()).thenReturn(true);

    this.logger.warn().message("x-warn-msg-x").log();

    Mockito.verify(this.slf4jLogger).warn(Mockito.contains("\"msg\":\"x-warn-msg-x\""));
  }

  @Test
  public void testWarnWhenDisabled() throws Exception {
    Mockito.when(this.slf4jLogger.isWarnEnabled()).thenReturn(false);

    JsonLogger result = this.logger.warn();

    assertTrue(result instanceof NoopLogger);
  }

  @Test
  public void testErrorWhenEnabled() throws Exception {
    Mockito.when(this.slf4jLogger.isErrorEnabled()).thenReturn(true);

    this.logger.error().message("x-error-msg-x").log();

    Mockito.verify(this.slf4jLogger).error(Mockito.contains("\"msg\":\"x-error-msg-x\""));
  }

  @Test
  public void testErrorWhenDisabled() throws Exception {
    Mockito.when(this.slf4jLogger.isErrorEnabled()).thenReturn(false);

    JsonLogger result = this.logger.error();

    assertTrue(result instanceof NoopLogger);
  }
}