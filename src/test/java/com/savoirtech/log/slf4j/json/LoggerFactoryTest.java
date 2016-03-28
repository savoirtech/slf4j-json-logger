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

package com.savoirtech.log.slf4j.json;

import com.savoirtech.log.slf4j.json.logger.Logger;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Validate the operation of the logger factory.
 *
 * Created by art on 3/28/16.
 */
public class LoggerFactoryTest {

  /**
   * Test the constructor purely for code-coverage.
   */
  @Test
  public void testConstructor() {
    new LoggerFactory();
  }

  @Test
  public void testGetLoggerByClass() throws Exception {
    Logger result = LoggerFactory.getLogger(LoggerFactoryTest.class);
    org.slf4j.Logger slf4jLogger = (org.slf4j.Logger) Whitebox.getInternalState(result,
                                                                                "slf4jLogger");

    assertNotNull(slf4jLogger);
    assertEquals(LoggerFactoryTest.class.getName(), slf4jLogger.getName());
  }

  @Test
  public void testGetLoggerByName() throws Exception {
    Logger result = LoggerFactory.getLogger("LoggerFactoryTest");
    org.slf4j.Logger slf4jLogger = (org.slf4j.Logger) Whitebox.getInternalState(result,
                                                                                "slf4jLogger");

    assertNotNull(slf4jLogger);
    assertEquals("LoggerFactoryTest", slf4jLogger.getName());
  }

  @Test
  public void testSetDateFormatString() throws Exception {
    LoggerFactory.setDateFormatString("HH:MM");

    //
    // WARNING: ugly reflection to validate the result here.
    //
    Field formatterField = LoggerFactory.class.getDeclaredField("formatter");
    formatterField.setAccessible(true);

    FastDateFormat formatter = (FastDateFormat) formatterField.get(null);

    assertEquals("HH:MM", formatter.getPattern());
  }
}