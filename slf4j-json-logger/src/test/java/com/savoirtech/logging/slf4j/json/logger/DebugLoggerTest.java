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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * Verify all of the operations of the DebugLogger itself.  Note this intentionally does not
 * test any of AbstractJsonLogger.
 *
 * Created by art on 3/28/16.
 */
public class DebugLoggerTest {

  private DebugLogger logger;

  private String testMessage;

  private org.slf4j.Logger slf4jLogger;

  private Gson gson;

  @Before
  public void setupTest() throws Exception {
    this.testMessage = "x-test-formatted-message-x";
    this.slf4jLogger = Mockito.mock(org.slf4j.Logger.class);
    this.gson = new GsonBuilder().disableHtmlEscaping().create();

    this.logger = new DebugLogger(slf4jLogger, null, gson) {
      @Override
      protected String formatMessage(String level) {
        if (level.equals(DebugLogger.LOG_LEVEL)) {
          return testMessage;
        } else {
          throw new RuntimeException("unexpected log level " + level);
        }
      }
    };
  }

  @Test
  public void testLog() throws Exception {
    this.logger.log();

    Mockito.verify(slf4jLogger).debug(this.testMessage);
  }

  @Test
  public void testToString() throws Exception {
    assertEquals(this.testMessage, this.logger.toString());
  }
}