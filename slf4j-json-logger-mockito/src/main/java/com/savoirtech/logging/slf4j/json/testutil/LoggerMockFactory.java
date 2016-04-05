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

import com.google.gson.JsonElement;

import com.savoirtech.logging.slf4j.json.logger.JsonLogger;
import com.savoirtech.logging.slf4j.json.logger.Logger;

import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Factory for simple creation of JsonLogger mocks that properly return the mock on all of the
 * chained method calls.
 *
 * Created by art on 3/31/16.
 */
public class LoggerMockFactory {

  private JsonLoggerMockFactory jsonLoggerMockFactory = new JsonLoggerMockFactory();

  public JsonLoggerMockFactory getJsonLoggerMockFactory() {
    return jsonLoggerMockFactory;
  }

  public void setJsonLoggerMockFactory(
      JsonLoggerMockFactory jsonLoggerMockFactory) {
    this.jsonLoggerMockFactory = jsonLoggerMockFactory;
  }

  public Logger createLoggerMock() {
    Logger result = Mockito.mock(Logger.class);
    JsonLogger mockJsonLogger = this.jsonLoggerMockFactory.createJsonLoggerMock();

    Mockito.when(result.trace()).thenReturn(mockJsonLogger);
    Mockito.when(result.debug()).thenReturn(mockJsonLogger);
    Mockito.when(result.info()).thenReturn(mockJsonLogger);
    Mockito.when(result.warn()).thenReturn(mockJsonLogger);
    Mockito.when(result.error()).thenReturn(mockJsonLogger);

    return result;
  }
}
