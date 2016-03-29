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

import com.savoirtech.logging.slf4j.json.DebugLogger;
import com.savoirtech.logging.slf4j.json.ErrorLogger;
import com.savoirtech.logging.slf4j.json.InfoLogger;
import com.savoirtech.logging.slf4j.json.JsonLogger;
import com.savoirtech.logging.slf4j.json.Logger;
import com.savoirtech.logging.slf4j.json.NoopLogger;
import com.savoirtech.logging.slf4j.json.TraceLogger;
import com.savoirtech.logging.slf4j.json.WarnLogger;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
  public void testTraceWhenEnabled() throws Exception {
    Mockito.when(this.slf4jLogger.isTraceEnabled()).thenReturn(true);

    JsonLogger result = this.logger.trace();

    assertTrue(result instanceof TraceLogger);
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

    JsonLogger result = this.logger.debug();

    assertTrue(result instanceof DebugLogger);
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

    JsonLogger result = this.logger.info();

    assertTrue(result instanceof InfoLogger);
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

    JsonLogger result = this.logger.warn();

    assertTrue(result instanceof WarnLogger);
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

    JsonLogger result = this.logger.error();

    assertTrue(result instanceof ErrorLogger);
  }

  @Test
  public void testErrorWhenDisabled() throws Exception {
    Mockito.when(this.slf4jLogger.isErrorEnabled()).thenReturn(false);

    JsonLogger result = this.logger.error();

    assertTrue(result instanceof NoopLogger);
  }
}